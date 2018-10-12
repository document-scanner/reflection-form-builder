/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.richtercloud.reflection.form.builder.retriever;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The specification of field groups on a class level allows specification at
 * a meaningful location. Specification at field level would cause redundancy
 * since there'd be no place that can assure that an ordering has been defined.
 *
 * Field group specifications only have effect for all classes on the
 * inheritance hierarchy which ensures that no unwanted behaviour has been
 * defined since reusing groups from other inheritance hierarchies is not
 * intuitive and makes field group ordering unnecessarily complicated because it
 * might reference groups which aren't inside the inheritance hierarchy. Even
 * though the groups on other inheritance hiararchies don't have any effect, all
 * field group names on all entity classes need to be disjoint in order to avoid
 * unwanted behaviour.
 *
 * @author richter
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FieldGroups {

    FieldGroup[] fieldGroups();
}
