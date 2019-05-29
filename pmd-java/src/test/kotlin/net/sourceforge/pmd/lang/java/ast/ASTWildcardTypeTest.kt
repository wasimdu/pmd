/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.ast

import net.sourceforge.pmd.lang.ast.test.shouldBe
import net.sourceforge.pmd.lang.java.ast.JavaVersion.*
import net.sourceforge.pmd.lang.java.ast.JavaVersion.Companion.Latest
import net.sourceforge.pmd.lang.java.ast.ParserTestCtx.Companion.TypeParsingCtx

/**
 * @author Clément Fournier
 * @since 7.0.0
 */
class ASTWildcardTypeTest : ParserTestSpec({

    parserTest("Test simple names", javaVersions = J1_5..Latest) {

        inContext(TypeParsingCtx) {
            "List<? extends B>" should parseAs {

                classType("List") {
                    typeArgList {
                        child<ASTWildcardType> {
                            it::hasUpperBound shouldBe true
                            it::hasLowerBound shouldBe false

                            it::getTypeBoundNode shouldBe classType("B")
                        }
                    }
                }
            }

            "List<? super B>" should parseAs {

                classType("List") {
                    typeArgList {
                        child<ASTWildcardType> {
                            it::hasUpperBound shouldBe false
                            it::hasLowerBound shouldBe true

                            it::getTypeBoundNode shouldBe classType("B")
                        }
                    }
                }
            }

            "List<?>" should parseAs {

                classType("List") {
                    typeArgList {
                        child<ASTWildcardType> {
                            it::hasUpperBound shouldBe false
                            it::hasLowerBound shouldBe false

                            it::getTypeBoundNode shouldBe null
                        }
                    }
                }
            }

            "List<? extends B & C>" shouldNot parse()
        }
    }

    parserTest("Annotation placement", javaVersions = J1_8..Latest) {

        inContext(TypeParsingCtx) {
            "List<@A @B ? extends B>" should parseAs {

                classType("List") {
                    typeArgList {
                        child<ASTWildcardType> {

                            annotationList {
                                annotation { }
                                annotation { }
                            }

                            it::hasUpperBound shouldBe true
                            it::hasLowerBound shouldBe false

                            it::getTypeBoundNode shouldBe classType("B")
                        }
                    }
                }
            }
        }
    }


})
