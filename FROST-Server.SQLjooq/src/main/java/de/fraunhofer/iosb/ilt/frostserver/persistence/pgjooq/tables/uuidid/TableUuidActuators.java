package de.fraunhofer.iosb.ilt.frostserver.persistence.pgjooq.tables.uuidid;

import de.fraunhofer.iosb.ilt.frostserver.persistence.pgjooq.tables.AbstractTableActuators;
import java.util.UUID;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

public class TableUuidActuators extends AbstractTableActuators<UUID> {

    private static final long serialVersionUID = 1850108682;

    /**
     * The reference instance of <code>public.ACTUATORS</code>
     */
    public static final TableUuidActuators ACTUATORS = new TableUuidActuators();

    /**
     * The column <code>public.ACTUATORS.ID</code>.
     */
    public final TableField<Record, UUID> id = createField(DSL.name("ID"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field("uuid_generate_v1mc()", SQLDataType.UUID)), this, "");

    /**
     * Create a <code>public.ACTUATORS</code> table reference
     */
    public TableUuidActuators() {
        super();
    }

    /**
     * Create an aliased <code>public.ACTUATORS</code> table reference
     *
     * @param alias The alias to use in queries.
     */
    public TableUuidActuators(Name alias) {
        this(alias, ACTUATORS);
    }

    private TableUuidActuators(Name alias, TableUuidActuators aliased) {
        super(alias, aliased);
    }

    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    @Override
    public TableField<Record, UUID> getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUuidActuators as(String alias) {
        return new TableUuidActuators(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUuidActuators as(Name alias) {
        return new TableUuidActuators(alias, this);
    }

}
