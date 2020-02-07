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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author jab
 */
public class ResourcePath {

    /**
     * Base root URI of the serivce.
     */
    private String serviceRootUrl;
    /**
     * The url that was used to generate this path.
     */
    private String pathUrl;
    /**
     * Flag indicating there was a $ref at the end of the path.
     */
    private boolean ref;
    /**
     * Flag indicating there was a $value at the end of the path.
     */
    private boolean value;
    /**
     * Flag indicating the path points to an entityProperty
     * (EntitySet(id)/entityProperty).
     */
    private boolean entityProperty;
    /**
     * The elements in this path.
     */
    private List<PathElement> pathElements;
    /**
     * The "main" element specified by this path. This is either an Entity or an
     * EntitySet, so it might not be the last element in the path.
     */
    private PathElement mainElement;
    /**
     * The "last" element in this path that had a specified id.
     */
    private PathElementEntity identifiedElement;

    public ResourcePath() {
        pathElements = new ArrayList<>();
    }

    public ResourcePath(String serviceRootUrl, String pathUrl) {
        pathElements = new ArrayList<>();
        this.serviceRootUrl = serviceRootUrl;
        this.pathUrl = pathUrl;
    }

    /**
     * Flag indicating there was a $ref at the end of the path.
     *
     * @return the ref
     */
    public boolean isRef() {
        return ref;
    }

    /**
     * Flag indicating there was a $ref at the end of the path.
     *
     * @param ref the ref to set
     */
    public void setRef(boolean ref) {
        this.ref = ref;
    }

    /**
     * Flag indicating there was a $value at the end of the path.
     *
     * @return the value
     */
    public boolean isValue() {
        return value;
    }

    /**
     * Flag indicating there was a $value at the end of the path.
     *
     * @param value the value to set
     */
    public void setValue(boolean value) {
        this.value = value;
    }

    /**
     * Flag indicating the path points to an entityProperty
     * (EntitySet(id)/entityProperty).
     *
     * @return the entityProperty
     */
    public boolean isEntityProperty() {
        return entityProperty;
    }

    /**
     * Returns the number of elements in the path.
     *
     * @return The size of the path.
     */
    public int size() {
        return pathElements.size();
    }

    public boolean isEmpty() {
        return pathElements.isEmpty();
    }

    /**
     * Get the element with the given index, where the first element has index
     * 0.
     *
     * @param index The index of the element to get.
     * @return The element with the given index.
     */
    public PathElement get(int index) {
        return pathElements.get(index);
    }

    public PathElement getMainElement() {
        return mainElement;
    }

    public EntityType getMainElementType() {
        if (mainElement instanceof PathElementEntity) {
            PathElementEntity entityPathElement = (PathElementEntity) mainElement;
            return entityPathElement.getEntityType();
        }
        if (mainElement instanceof PathElementEntitySet) {
            PathElementEntitySet entitySetPathElement = (PathElementEntitySet) mainElement;
            return entitySetPathElement.getEntityType();
        }
        return null;
    }

    public PathElement getLastElement() {
        if (pathElements.isEmpty()) {
            return null;
        }
        return pathElements.get(pathElements.size() - 1);
    }

    public PathElementEntity getIdentifiedElement() {
        return identifiedElement;
    }

    public void setMainElement(PathElement mainElementType) {
        this.mainElement = mainElementType;
    }

    public void setIdentifiedElement(PathElementEntity identifiedElement) {
        this.identifiedElement = identifiedElement;
    }

    /**
     * Add the given element at the given index.
     *
     * @param index The position in the path to put the element.
     * @param pe The element to add.
     */
    public void addPathElement(int index, PathElement pe) {
        pathElements.add(index, pe);
    }

    public void addPathElement(PathElement pe) {
        addPathElement(pe, false, false);
    }

    /**
     * Add the given path element, optionally setting it as the main element, or
     * as the identifying element.
     *
     * @param pe The element to add.
     * @param isMain Flag indicating it is the main element.
     * @param isIdentifier Flag indicating it is the identifying element.
     */
    public void addPathElement(PathElement pe, boolean isMain, boolean isIdentifier) {
        pathElements.add(pe);
        if (isMain && pe instanceof PathElementEntity || pe instanceof PathElementEntitySet) {
            setMainElement(pe);
        }
        if (isIdentifier && pe instanceof PathElementEntity) {
            PathElementEntity epe = (PathElementEntity) pe;
            setIdentifiedElement(epe);
        }
        this.entityProperty = (pe instanceof PathElementProperty);
    }

    public void compress() {
        for (int i = pathElements.size() - 1; i > 0; i--) {
            if (pathElements.get(i) instanceof PathElementEntity
                    && pathElements.get(i - 1) instanceof PathElementEntitySet) {
                PathElementEntity epe = (PathElementEntity) pathElements.get(i);
                if (epe.getId() != null) {
                    // crop path
                    setMainElement(pathElements.get(i - 1));
                    pathElements.subList(0, i - 1).clear();
                    setIdentifiedElement(epe);
                    return;
                }
            }
        }
    }

    public String getServiceRootUrl() {
        return serviceRootUrl;
    }

    public void setServiceRootUrl(String serviceRootUrl) {
        this.serviceRootUrl = serviceRootUrl;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceRootUrl, ref, value, pathElements, mainElement, identifiedElement);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResourcePath other = (ResourcePath) obj;
        return Objects.equals(this.serviceRootUrl, other.serviceRootUrl)
                && this.ref == other.ref
                && this.value == other.value
                && Objects.equals(this.pathElements, other.pathElements)
                && Objects.equals(this.mainElement, other.mainElement)
                && Objects.equals(this.identifiedElement, other.identifiedElement);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(serviceRootUrl);
        for (PathElement rpe : pathElements) {
            if (rpe instanceof PathElementEntity && ((PathElementEntity) rpe).getId() != null) {
                PathElementEntity epe = (PathElementEntity) rpe;
                sb.append("(").append(epe.getId().getUrl()).append(")");
            } else if (rpe instanceof PathElementArrayIndex) {
                sb.append(rpe.toString());
            } else {
                sb.append("/");
                sb.append(rpe.toString());
            }
        }
        if (isRef()) {
            sb.append("/$ref");
        } else if (isValue()) {
            sb.append("/$value");
        }
        return sb.toString();
    }

}
