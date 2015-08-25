/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package richtercloud.reflection.form.builder;

/**
 *
 * @author richter
 */
/*
internal implementation notes:
- needs to be a proper class in order to have a constructor in reflection API
*/
class TestEntitySubclass extends TestEntity {
    private String b = "bb";

    TestEntitySubclass() {
    }
    
}
