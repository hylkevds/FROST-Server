package de.fraunhofer.iosb.ilt.frostserver.persistence.pgjooq.tables.uuidid;

import de.fraunhofer.iosb.ilt.frostserver.persistence.pgjooq.tables.AbstractTableFeatures;
import java.util.UUID;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

public class TableUuidFeatures extends AbstractTableFeatures<UUID> {

    private static final long serialVersionUID = 750481677;

    /**
     * The reference instance of <code>public.FEATURES</code>
     */
    public static final TableUuidFeatures FEATURES = new TableUuidFeatures();

    /**
     * The column <code>public.FEATURES.ID</code>.
     */
    public final TableField<Record, UUID> id = createField(DSL.name("ID"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field("uuid_generate_v1mc()", SQLDataType.UUID)), this, "");

    /**
     * Create a <code>public.FEATURES</code> table reference
     */
    public TableUuidFeatures() {
        super();
    }

    /**
     * Create an aliased <code>public.FEATURES</code> table reference
     *
     * @param alias The alias to use in queries.
     */
    public TableUuidFeatures(Name alias) {
        this(alias, FEATURES);
    }

    private TableUuidFeatures(Name alias, TableUuidFeatures aliased) {
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
    public TableUuidFeatures as(String alias) {
        return new TableUuidFeatures(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUuidFeatures as(Name alias) {
        return new TableUuidFeatures(alias, this);
    }

}
