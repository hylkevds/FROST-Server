package de.fraunhofer.iosb.ilt.frostserver.persistence.pgjooq.tables.longid;

import de.fraunhofer.iosb.ilt.frostserver.persistence.pgjooq.tables.AbstractTableLocationsHistLocations;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

public class TableLongLocationsHistLocations extends AbstractTableLocationsHistLocations<Long> {

    private static final long serialVersionUID = -1022733888;

    /**
     * The reference instance of <code>public.LOCATIONS_HIST_LOCATIONS</code>
     */
    public static final TableLongLocationsHistLocations LOCATIONS_HIST_LOCATIONS = new TableLongLocationsHistLocations();

    /**
     * The column <code>public.LOCATIONS_HIST_LOCATIONS.LOCATION_ID</code>.
     */
    public final TableField<Record, Long> locationId = createField(DSL.name("LOCATION_ID"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.LOCATIONS_HIST_LOCATIONS.HIST_LOCATION_ID</code>.
     */
    public final TableField<Record, Long> histLocationId = createField(DSL.name("HIST_LOCATION_ID"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * Create a <code>public.LOCATIONS_HIST_LOCATIONS</code> table reference
     */
    public TableLongLocationsHistLocations() {
        super();
    }

    /**
     * Create an aliased <code>public.LOCATIONS_HIST_LOCATIONS</code> table
     * reference
     *
     * @param alias The alias to use in queries.
     */
    public TableLongLocationsHistLocations(Name alias) {
        this(alias, LOCATIONS_HIST_LOCATIONS);
    }

    private TableLongLocationsHistLocations(Name alias, TableLongLocationsHistLocations aliased) {
        super(alias, aliased);
    }

    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    @Override
    public TableField<Record, Long> getLocationId() {
        return locationId;
    }

    @Override
    public TableField<Record, Long> getHistLocationId() {
        return histLocationId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableLongLocationsHistLocations as(String alias) {
        return new TableLongLocationsHistLocations(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableLongLocationsHistLocations as(Name alias) {
        return new TableLongLocationsHistLocations(alias, this);
    }

}
