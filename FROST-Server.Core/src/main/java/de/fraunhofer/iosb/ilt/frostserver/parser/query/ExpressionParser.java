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
package de.fraunhofer.iosb.ilt.frostserver.parser.query;

import de.fraunhofer.iosb.ilt.frostserver.property.CustomProperty;
import de.fraunhofer.iosb.ilt.frostserver.property.EntityProperty;
import de.fraunhofer.iosb.ilt.frostserver.property.Property;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.Expression;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.Path;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.constant.BooleanConstant;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.constant.Constant;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.constant.DateConstant;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.constant.DateTimeConstant;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.constant.DoubleConstant;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.constant.DurationConstant;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.constant.GeoJsonConstant;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.constant.IntegerConstant;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.constant.IntervalConstant;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.constant.StringConstant;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.constant.TimeConstant;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.Function;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.FunctionTypeBinding;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.arithmetic.Add;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.arithmetic.Divide;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.arithmetic.Modulo;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.arithmetic.Multiply;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.arithmetic.Subtract;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.comparison.Equal;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.comparison.GreaterEqual;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.comparison.GreaterThan;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.comparison.LessEqual;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.comparison.LessThan;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.comparison.NotEqual;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.date.Date;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.date.Day;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.date.FractionalSeconds;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.date.Hour;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.date.MaxDateTime;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.date.MinDateTime;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.date.Minute;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.date.Month;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.date.Now;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.date.Second;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.date.Time;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.date.TotalOffsetMinutes;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.date.Year;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.logical.And;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.logical.Not;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.logical.Or;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.math.Ceiling;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.math.Floor;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.math.Round;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.spatialrelation.GeoDistance;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.spatialrelation.GeoIntersects;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.spatialrelation.GeoLength;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.spatialrelation.STContains;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.spatialrelation.STCrosses;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.spatialrelation.STDisjoint;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.spatialrelation.STEquals;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.spatialrelation.STIntersects;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.spatialrelation.STOverlaps;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.spatialrelation.STRelate;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.spatialrelation.STTouches;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.spatialrelation.STWithin;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.string.Concat;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.string.EndsWith;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.string.IndexOf;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.string.Length;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.string.StartsWith;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.string.Substring;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.string.SubstringOf;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.string.ToLower;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.string.ToUpper;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.string.Trim;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.temporal.After;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.temporal.Before;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.temporal.During;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.temporal.Finishes;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.temporal.Meets;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.temporal.Overlaps;
import de.fraunhofer.iosb.ilt.frostserver.query.expression.function.temporal.Starts;
import de.fraunhofer.iosb.ilt.frostserver.util.ParserHelper;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;

/**
 *
 * @author jab
 */
public class ExpressionParser extends AbstractParserVisitor {

    public enum Operator {
        // Logical
        OP_NOT("not", Not.class),
        OP_AND("and", And.class),
        OP_OR("or", Or.class),
        // Math
        OP_ADD("+", Add.class),
        OP_SUB("-", Subtract.class),
        OP_MUL("mul", Multiply.class),
        OP_DIV("div", Divide.class),
        OP_MOD("mod", Modulo.class),
        // Comparison
        OP_EQUAL("eq", Equal.class),
        OP_NOT_EQUAL("ne", NotEqual.class),
        OP_GREATER_THAN("gt", GreaterThan.class),
        OP_GREATER_EQUAL("ge", GreaterEqual.class),
        OP_LESS_THAN("lt", LessThan.class),
        OP_LESS_EQUAL("le", LessEqual.class),
        // String
        OP_SUBSTRING_OF("substringof", SubstringOf.class),
        OP_ENDS_WITH("endswith", EndsWith.class),
        OP_STARTS_WITH("startswith", StartsWith.class),
        OP_LENGTH("length", Length.class),
        OP_INDEX_OF("indexof", IndexOf.class),
        OP_SUBSTRING("substring", Substring.class),
        OP_TO_LOWER("tolower", ToLower.class),
        OP_TO_UPPER("toupper", ToUpper.class),
        OP_TRIM("trim", Trim.class),
        OP_CONCAT("concat", Concat.class),
        // DateTime
        OP_YEAR("year", Year.class),
        OP_MONTH("month", Month.class),
        OP_DAY("day", Day.class),
        OP_HOUR("hour", Hour.class),
        OP_MINUTE("minute", Minute.class),
        OP_SECOND("second", Second.class),
        OP_FRACTIONAL_SECONDS("fractionalseconds", FractionalSeconds.class),
        OP_DATE("date", Date.class),
        OP_TIME("time", Time.class),
        OP_TOTAL_OFFSET_MINUTES("totaloffsetminutes", TotalOffsetMinutes.class),
        OP_NOW("now", Now.class),
        OP_MIN_DATETIME("mindatetime", MinDateTime.class),
        OP_MAX_DATETIME("maxdatetime", MaxDateTime.class),
        // Allen's interval algebra
        OP_BEFORE("before", Before.class),
        OP_AFTER("after", After.class),
        OP_MEETS("meets", Meets.class),
        OP_DURING("during", During.class),
        OP_OVERLAPS("overlaps", Overlaps.class),
        OP_STARTS("starts", Starts.class),
        OP_FINISHES("finishes", Finishes.class),
        // Math
        OP_ROUND("round", Round.class),
        OP_FLOOR("floor", Floor.class),
        OP_CEILING("ceiling", Ceiling.class),
        // Geo
        OP_GEO_DISTANCE("geo.distance", GeoDistance.class),
        OP_GEO_LENGTH("geo.length", GeoLength.class),
        OP_GEO_INTERSECTS("geo.intersects", GeoIntersects.class),
        OP_ST_EQUALS("st_equals", STEquals.class),
        OP_ST_DISJOINT("st_disjoint", STDisjoint.class),
        OP_ST_TOUCHES("st_touches", STTouches.class),
        OP_ST_WITHIN("st_within", STWithin.class),
        OP_ST_OVERLAPS("st_overlaps", STOverlaps.class),
        OP_ST_CROSSES("st_crosses", STCrosses.class),
        OP_ST_INTERSECTS("st_intersects", STIntersects.class),
        OP_ST_CONTAINS("st_contains", STContains.class),
        OP_ST_RELATE("st_relate", STRelate.class);

        private static final Map<String, Operator> byKey = new HashMap<>();

        static {
            for (Operator o : Operator.values()) {
                byKey.put(o.urlKey, o);
            }
        }
        public final String urlKey;
        public final Class<? extends Function> implementingClass;

        private Operator(String urlKey, Class<? extends Function> implementingClass) {
            this.urlKey = urlKey;
            this.implementingClass = implementingClass;
        }

        public Function instantiate() {
            try {
                return implementingClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
                throw new IllegalStateException("problem executing '" + this + "'", ex);
            }
        }

        public static Operator fromKey(String key) {
            Operator operator = byKey.get(key);
            if (operator == null) {
                throw new IllegalArgumentException("Unknown operator: '" + key + "'.");
            }
            return operator;
        }
    }

    public static Expression parseExpression(Node node) {
        return new ExpressionParser().visit(node, null);
    }

    @Override
    public Path visit(ASTPlainPath node, Object data) {
        Path path = new Path();
        Property previous = null;
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            Node child = node.jjtGetChild(i);
            if (!(child instanceof ASTPathElement)) {
                throw new IllegalArgumentException("alle childs of ASTPlainPath must be of type ASTPathElement");
            }
            Property property = visit((ASTPathElement) child, previous);
            if (property instanceof CustomProperty) {
                if (!(previous instanceof EntityProperty) && !(previous instanceof CustomProperty)) {
                    throw new IllegalArgumentException("Custom properties (" + property.getName() + ") are only allowed below entity properties or other custom properties.");
                }
                if (previous instanceof EntityProperty && !((EntityProperty) previous).hasCustomProperties) {
                    throw new IllegalArgumentException("Entity property " + previous.getName() + " does not have custom properties (" + property.getName() + ").");
                }
            }
            path.getElements().add(property);
            previous = property;
        }
        return path;
    }

    @Override
    public Property visit(ASTPathElement node, Object data) {
        if (node.getIdentifier() != null && !node.getIdentifier().isEmpty()) {
            throw new IllegalArgumentException("no identified paths are allowed inside expressions");
        }
        Property previous = null;
        if (data instanceof Property) {
            previous = (Property) data;
        }
        return ParserHelper.parseProperty(node.getName(), previous);
    }

    @Override
    public Expression visit(ASTFilter node, Object data) {
        if (node.jjtGetNumChildren() != 1) {
            throw new IllegalArgumentException("filter node must have exactly one child!");
        }
        return visit(node.jjtGetChild(0), data);
    }

    @Override
    public Or visit(ASTLogicalOr node, Object data) {
        return (Or) visitLogicalFunction(Operator.OP_OR, node, data);
    }

    @Override
    public And visit(ASTLogicalAnd node, Object data) {
        return (And) visitLogicalFunction(Operator.OP_AND, node, data);
    }

    private Function visitLogicalFunction(Operator operator, Node node, Object data) {
        if (node.jjtGetNumChildren() < 2) {
            throw new IllegalArgumentException("'" + operator + "' must have at least two parameters");
        }
        Function function = operator.instantiate();
        Expression result = visitChildWithType(function, node.jjtGetChild(node.jjtGetNumChildren() - 1), data, 1);
        for (int i = node.jjtGetNumChildren() - 2; i >= 0; i--) {
            function = operator.instantiate();
            Expression lhs = visitChildWithType(function, node.jjtGetChild(i), data, 0);
            function.setParameters(lhs, result);
            result = function;
        }

        return (Function) result;
    }

    @Override
    public Function visit(ASTNot node, Object data) {
        if (node.jjtGetNumChildren() != 1) {
            throw new IllegalArgumentException("'not' must have exactly one parameter");
        }
        return visit((ASTFunction) node, data);
    }

    @Override
    public Function visit(ASTBooleanFunction node, Object data) {
        return visit((ASTFunction) node, data);
    }

    @Override
    public Function visit(ASTComparison node, Object data) {
        if (node.jjtGetNumChildren() != 2) {
            throw new IllegalArgumentException("comparison must have exactly 2 children");
        }
        return visit((ASTFunction) node, data);
    }

    private Expression visit(Node node, Object data) {
        return (Expression) node.jjtAccept(this, data);
    }

    private Function visitArithmeticFunction(SimpleNode node, Object data) {
        int childCount = node.jjtGetNumChildren();
        if (childCount < 3 || childCount % 2 == 0) {
            throw new IllegalArgumentException("add/sub with wrong number of arguments");
        }
        // can be n-ary relation -> need to binaryfy
        // backwards iteration over children incl. visit(this) to handle expressions
        Expression rhs;
        Expression lhs;
        Function result = null;
        int idx = 0;
        while (idx < childCount) {
            int operatorIndex = result == null ? idx + 1 : idx;
            if (!(node.jjtGetChild(operatorIndex) instanceof ASTOperator)) {
                throw new IllegalArgumentException("operator expected but '" + node.jjtGetChild(idx).getClass().getName() + "' found");
            }
            String operatorKey = ((ASTOperator) node.jjtGetChild(operatorIndex)).getName().trim().toLowerCase();
            Function function = getFunction(operatorKey);

            if (result == null) {
                lhs = visitChildWithType(function, node.jjtGetChild(idx), data, 1);
                idx++;
            } else {
                lhs = result;
            }
            idx++;
            rhs = visitChildWithType(function, node.jjtGetChild(idx), data, 0);
            function.setParameters(lhs, rhs);
            result = function;
            idx++;
        }
        return result;
    }

    @Override
    public Function visit(ASTPlusMin node, Object data) {
        return visitArithmeticFunction(node, data);
    }

    @Override
    public Function visit(ASTMulDiv node, Object data) {
        return visitArithmeticFunction(node, data);
    }

    private Function getFunction(String operator) {
        return Operator.fromKey(operator).instantiate();
    }

    @Override
    public Function visit(ASTFunction node, Object data) {
        String operator = node.getName().trim().toLowerCase();
        Function function = getFunction(operator);
        function.setParameters(visitChildsWithType(function, node, data));
        return function;
    }

    private Expression[] visitChildsWithType(Function function, Node node, Object data) {
        List<FunctionTypeBinding> allowedBindings = function.getAllowedTypeBindings();
        if (data != null) {
            List<Class<? extends Constant>> allowedReturnTypes = (List<Class<? extends Constant>>) data;
            allowedBindings = allowedBindings.stream().filter(x -> allowedReturnTypes.contains(x.getReturnType())).collect(Collectors.toList());
        }
        Expression[] parameters = new Expression[node.jjtGetNumChildren()];
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = visit(node.jjtGetChild(i), allowedBindings.stream().map(x -> x.getParameters().get(0)).collect(Collectors.toList()));
        }
        return parameters;
    }

    private Expression visitChildWithType(Function function, Node child, Object data, int parameterIndex) {
        List<FunctionTypeBinding> allowedBindings = function.getAllowedTypeBindings();
        if (data != null) {
            List<Class<? extends Constant>> allowedReturnTypes = (List<Class<? extends Constant>>) data;
            allowedBindings = allowedBindings.stream().filter(x -> allowedReturnTypes.contains(x.getReturnType())).collect(Collectors.toList());
        }
        return visit(child, allowedBindings.stream().map(x -> x.getParameters().get(parameterIndex)).collect(Collectors.toList()));
    }

    @Override
    public Constant visit(ASTValueNode node, Object data) {
        Object value = node.jjtGetValue();
        if (value instanceof Boolean) {
            return new BooleanConstant((Boolean) value);
        } else if (value instanceof Double) {
            return new DoubleConstant((Double) value);
        } else if (value instanceof Integer) {
            return new IntegerConstant((Integer) value);
        } else if (value instanceof Long) {
            return new IntegerConstant(((Number) value).intValue());
        } else if (value instanceof DateTime) {
            return new DateTimeConstant((DateTime) value);
        } else if (value instanceof LocalDate) {
            return new DateConstant((LocalDate) value);
        } else if (value instanceof LocalTime) {
            return new TimeConstant((LocalTime) value);
        } else if (value instanceof Period) {
            return new DurationConstant((Period) value);
        } else if (value instanceof Interval) {
            return new IntervalConstant((Interval) value);
        } else {
            return new StringConstant(node.jjtGetValue().toString());
        }
    }

    private static final String GEOGRAPHY_REGEX = "^geography\\s*'\\s*(.*)'$";
    private static final Pattern GEORAPHY_PATTERN = Pattern.compile(GEOGRAPHY_REGEX);

    @Override
    public GeoJsonConstant visit(ASTGeoStringLit node, Object data) {
        String raw = node.jjtGetValue().toString().trim();
        Matcher matcher = GEORAPHY_PATTERN.matcher(raw);
        if (matcher.matches()) {
            return GeoJsonConstant.fromString(matcher.group(1).trim());
        } else {
            throw new IllegalArgumentException("invalid geography string '" + raw + "'");
        }
    }

    @Override
    public BooleanConstant visit(ASTBool node, Object data) {
        return new BooleanConstant(node.getValue());
    }

}
