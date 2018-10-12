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
 * Allows specification of a field group which should take place on the class
 * level (see {@link FieldGroups} for further explanation). The specification of
 * contained fields does take place on the field directly with a reference to
 * the group in order to allow adding fields in subclasses (which is much easier
 * if just the reference to the group defined on the superclass needs to be
 * passed instead of developing an override mechanism of {@link FieldGroups} and
 * {@link FieldGroup} annotations on subclasses) and improving readability of
 * information about group membership directly on the field.
 *
 * @see FieldPosition for possible references between fields and groups
 * @author richter
 */
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldGroup {

    String name();

    /**
     * The list of field groups to be placed after this group.
     * @return the names of the groups
     */
    /*
    internal implementation notes:
    - references the groups by name in order to avoid cyclic reference which
    doesn't compile
    */
    String[] beforeGroups() default {};

    /**
     * The list of field groups to be placed before this group.
     * @return the names of the groups
     */
    /*
    internal implementation notes:
    - references the groups by name in order to avoid cyclic reference which
    doesn't compile
    */
    String[] afterGroups() default {};
}
