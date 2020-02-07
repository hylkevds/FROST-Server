/*
 * Copyright (C) 2016 Fraunhofer Institut IOSB, Fraunhoferstr. 1, D 76131
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
package de.fraunhofer.iosb.ilt.frostserver.path;

import de.fraunhofer.iosb.ilt.frostserver.model.EntityType;
import de.fraunhofer.iosb.ilt.frostserver.model.core.Id;
import java.util.Objects;

/**
 *
 * @author jab
 */
public class PathElementEntity implements PathElement {

    private Id id;
    private EntityType entityType;
    private PathElement parent;

    public PathElementEntity() {
    }

    public PathElementEntity(Id id, EntityType entityType, PathElement parent) {
        this.id = id;
        this.entityType = entityType;
        this.parent = parent;
    }

    public Id getId() {
        return id;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public PathElement getParent() {
        return parent;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public void setParent(PathElement parent) {
        this.parent = parent;
    }

    @Override
    public void visit(ResourcePathVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return entityType.entityName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityType, parent);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PathElementEntity other = (PathElementEntity) obj;
        return Objects.equals(this.id, other.id)
                && this.entityType == other.entityType
                && Objects.equals(this.parent, other.parent);
    }

}
