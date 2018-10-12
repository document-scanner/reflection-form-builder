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

/*
internal implementation notes:
- Limiting afterFields and beforeFields to groups inside the same group is an
support to reduce unwanted behaviour. It might require to add a field group in
some situations, but it seems worth it.
*/
/**
 * An annotation used to indicate the position in a {@link ReflectionFormPanel}
 * by specifying the membership in a group with the option to specify the
 * position within the group.
 *
 * <h1>Motivation and concept</h1>
 * Since absolute positioning is very intransparent and requires dealing with an
 * ever increasing set of position constants (either specified in classes or
 * pinned to the backside of your head), it's easier to specify relative
 * positions and groups and let a
 * {@link richtercloud.validation.tools.FieldRetriever} make sure that
 * everything makes sense based on validation.
 *
 * There might be a usecase for referencing fields which don't belong to a field
 * group from within a field of a field group or outside, but for ease sake it's
 * not supported, i.e. all ordering between groups takes place in
 * {@link FieldGroup} annotations and all ordering inside groups with
 * {@link #afterFields() } and {@link #beforeFields() }.
 *
 * If a {@link #fieldGroup() } is specified {@link FieldPosition#afterFields() }
 * and {@link FieldPosition#beforeFields() } have to reference fields inside the
 * group only.
 *
 * <h1>Tips</h1>
 * Specify group names as constants in a separate class in order to avoid
 * undesired behaviour because of typos.
 *
 * {@code FieldPosition}s without a {@link #fieldGroup() } reference and both an
 * empty value for {@link #afterFields() } and {@link #beforeFields() } are
 * invalid because they have no use.
 *
 * @see FieldGroups
 * @author richter
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD})
public @interface FieldPosition {

    /**
     * A reference to the group the field belongs to. It's mandatory to specify
     * a field group (see class comment for an explanation).
     * @return the name of the owning group
     */
    String fieldGroup();

    /**
     * The list of field names which should occur before the annotated field.
     * @return the list of preceeding fields
     */
    /*
    internal implementation notes:
    - empty by default in order to ease usage in FieldGroupItem where one might
    not want to be forced to specify order within a group in case it doesn't
    matter
    */
    String[] afterFields() default {};

    /**
     * The list of field names which should occur after the annotated field.
     * @return the list of following fields
     */
    /*
    internal implementation notes:
    - empty by default in order to ease usage in FieldGroupItem where one might
    not want to be forced to specify order within a group in case it doesn't
    matter
    */
    String[] beforeFields() default {};
}
