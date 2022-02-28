package com.kfabija.test

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test

class ArchTest {

    @Test
    fun servicesAndRepositoriesShouldNotDependOnWebLayer() {

        val importedClasses = ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.kfabija.test")

        noClasses()
            .that()
            .resideInAnyPackage("com.kfabija.test.service..")
            .or()
            .resideInAnyPackage("com.kfabija.test.repository..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..com.kfabija.test.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses)
    }
}
