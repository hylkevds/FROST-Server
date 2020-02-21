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
package de.fraunhofer.iosb.ilt.frostserver.query.expression.constant;

import de.fraunhofer.iosb.ilt.frostserver.util.TestHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author jab
 */
public class LineStringConstantTest {

    @Test
    public void testParseFromString2D() {
        String text = "LINESTRING (30 10, 10 30, 40 40)";
        LineStringConstant result = new LineStringConstant(text);
        Assert.assertEquals(TestHelper.getLine(new Integer[]{30, 10}, new Integer[]{10, 30}, new Integer[]{40, 40}), result.getValue());
    }

    @Test
    public void testParseFromString3D() {
        String text = "LINESTRING (30 10 10, 10 30 10, 40 40 40)";
        LineStringConstant result = new LineStringConstant(text);
        Assert.assertEquals(TestHelper.getLine(new Integer[]{30, 10, 10}, new Integer[]{10, 30, 10}, new Integer[]{40, 40, 40}), result.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromStringWithMixedDimensions() {
        String text = "LINESTRING (30 10, 10 30 40)";
        LineStringConstant lineStringConstant = new LineStringConstant(text);
        Assert.fail("Should have thrown an exception but got a value: " + lineStringConstant.toString());
    }

}
