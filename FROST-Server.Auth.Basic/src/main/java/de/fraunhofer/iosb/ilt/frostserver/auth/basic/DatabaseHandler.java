/*
 * Copyright (C) 2018 Fraunhofer Institut IOSB, Fraunhoferstr. 1, D 76131
 * Karlsruhe, Germany.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fraunhofer.iosb.ilt.frostserver.auth.basic;

import static de.fraunhofer.iosb.ilt.frostserver.auth.basic.BasicAuthProvider.LIQUIBASE_CHANGELOG_FILENAME;
import static de.fraunhofer.iosb.ilt.frostserver.auth.basic.BasicAuthProvider.TAG_AUTO_UPDATE_DATABASE;
import de.fraunhofer.iosb.ilt.frostserver.persistence.pgjooq.ConnectionUtils;
import de.fraunhofer.iosb.ilt.frostserver.persistence.pgjooq.LiquibaseHelper;
import de.fraunhofer.iosb.ilt.frostserver.settings.CoreSettings;
import de.fraunhofer.iosb.ilt.frostserver.settings.Settings;
import de.fraunhofer.iosb.ilt.frostserver.util.LiquibaseUtils;
import de.fraunhofer.iosb.ilt.frostserver.util.exception.UpgradeFailedException;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author scf
 */
public class DatabaseHandler {

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHandler.class);
    private static DatabaseHandler instance;

    private final CoreSettings coreSettings;
    private final ConnectionUtils.ConnectionWrapper connectionProvider;
    private DSLContext dslContext;
    private boolean maybeUpdateDatabase;

    public static void init(CoreSettings coreSettings) {
        if (instance == null) {
            createInstance(coreSettings);
        }
    }

    private static synchronized void createInstance(CoreSettings coreSettings) {
        if (instance == null) {
            LOGGER.error("Initialising DatabaseHandler.");
            instance = new DatabaseHandler(coreSettings);
        }
    }

    public static DatabaseHandler getInstance() {
        if (instance == null) {
            LOGGER.error("DatabaseHandler not initialised.");
        }
        return instance;
    }

    private DatabaseHandler(CoreSettings coreSettings) {
        this.coreSettings = coreSettings;
        Settings authSettings = coreSettings.getAuthSettings();

        maybeUpdateDatabase = authSettings.getBoolean(TAG_AUTO_UPDATE_DATABASE, BasicAuthProvider.class);
        connectionProvider = new ConnectionUtils.ConnectionWrapper(authSettings);
    }

    private synchronized DSLContext createDslContext() {
        if (dslContext == null) {
            dslContext = DSL.using(connectionProvider.get(), SQLDialect.POSTGRES);
        }
        return dslContext;
    }

    public DSLContext getDslContext() {
        if (dslContext == null) {
            createDslContext();
        }
        return dslContext;
    }

    public boolean isValidUser(String userName, String password) {
        maybeUpdateDatabase();
        try {
            Record1<Integer> one = getDslContext()
                    .selectOne()
                    .from(TableUsers.USERS)
                    .where(
                            TableUsers.USERS.userName.eq(userName)
                                    .and(TableUsers.USERS.userPass.eq(password))
                    ).fetchOne();
            return one != null;
        } catch (DataAccessException exc) {
            LOGGER.error("Failed to check user credentials.", exc);
            return false;
        }
    }

    /**
     * This method checks if the given user exists and has the given role.
     *
     * @param userName The username of the user to check the role for.
     * @param userPass The password of the user to check the role for.
     * @param roleName The role to check.
     * @return true if the user exists AND has the given password AND has the
     * given role.
     */
    public boolean userHasRole(String userName, String userPass, String roleName) {
        maybeUpdateDatabase();
        try {
            Record1<Integer> one = getDslContext()
                    .selectOne()
                    .from(TableUsers.USERS)
                    .leftJoin(TableUsersRoles.USER_ROLES)
                    .on(TableUsers.USERS.userName.eq(TableUsersRoles.USER_ROLES.userName))
                    .where(
                            TableUsers.USERS.userName.eq(userName)
                                    .and(TableUsers.USERS.userPass.eq(userPass))
                                    .and(TableUsersRoles.USER_ROLES.roleName.eq(roleName))
                    ).fetchOne();
            return one != null;
        } catch (DataAccessException exc) {
            LOGGER.error("Failed to check user rights.", exc);
            return false;
        } finally {
            connectionProvider.doRollback();
        }
    }

    public boolean userHasRole(String userName, String roleName) {
        try {
            Record1<Integer> one = createDslContext()
                    .selectOne()
                    .from(TableUsersRoles.USER_ROLES)
                    .where(
                            TableUsersRoles.USER_ROLES.userName.eq(userName)
                                    .and(TableUsersRoles.USER_ROLES.roleName.eq(roleName))
                    ).fetchOne();
            return one != null;
        } catch (RuntimeException exc) {
            LOGGER.error("Failed to check user rights.", exc);
            return false;
        }
    }

    private void maybeUpdateDatabase() {
        if (maybeUpdateDatabase) {
            BasicAuthProvider basicAuthProvider = new BasicAuthProvider();
            basicAuthProvider.init(coreSettings);
            maybeUpdateDatabase = LiquibaseUtils.maybeUpdateDatabase(LOGGER, basicAuthProvider);
        }
    }

    public String checkForUpgrades() {
        Settings customSettings = coreSettings.getAuthSettings();
        try (Connection connection = ConnectionUtils.getConnection("FROST-BasicAuth", customSettings)) {
            return LiquibaseHelper.checkForUpgrades(connection, LIQUIBASE_CHANGELOG_FILENAME);
        } catch (SQLException ex) {
            LOGGER.error("Could not initialise database.", ex);
            return "Failed to initialise database:\n"
                    + ex.getLocalizedMessage()
                    + "\n";
        }
    }

    public boolean doUpgrades(Writer out) throws UpgradeFailedException, IOException {
        Settings customSettings = coreSettings.getAuthSettings();
        try (Connection connection = ConnectionUtils.getConnection("FROST-BasicAuth", customSettings)) {
            return LiquibaseHelper.doUpgrades(connection, LIQUIBASE_CHANGELOG_FILENAME, out);
        } catch (SQLException ex) {
            LOGGER.error("Could not initialise database.", ex);
            out.append("Failed to initialise database:\n");
            out.append(ex.getLocalizedMessage());
            out.append("\n");
            return false;
        }
    }
}
