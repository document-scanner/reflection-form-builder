/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package richtercloud.reflection.form.builder;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows storage of information of class properties/fields (an official or
 * displayable name which is useful if the property is named following a
 * convention which produces ugly names and a description) which are displayed
 * in the {@link ReflectionFormPanel} (more concretely in the {@link
 * ReflectionFormFieldLabel}.
 *
 * @author richter
 * @see ClassInfo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD})
public @interface FieldInfo {
    /**
     * Override field name to appear more natural or better explaining the structure  of the data (e.g. {@code Finance accounts} might appear much more clear than {@code accounts}. {@code ""} indicates that the
     * field name ought to be used (because {@code null} can't be specified).
     * @return
     */
    String name() default "";

    public final static String DESCRIPTION_DEFAULT = "";

    /**
     * A field describing the function of the field in the entity or embeddable
     * and eventually explaining design decisions.
     * @return
     */
    /*
    internal implementation notes:
    - In order to be able to provide a mechanism to indicate that no description
    is provided (e.g. in order to hide controls in the field label, e.g. the ?
    button which doesn't have to be there if the description isn't provided),
    the value "" is used for that (null can't be used in annotations).
    */
    String description() default DESCRIPTION_DEFAULT;
}
