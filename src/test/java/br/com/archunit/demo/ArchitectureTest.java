package br.com.archunit.demo;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@SpringBootTest
@AnalyzeClasses(packages = "br.com.archunit.demo")
class DemoApplicationTests {

    @ArchTest
    private final ArchRule layer_dependencies_are_respected = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Controller").definedBy("..controller..")
            .layer("Service").definedBy("..service..")
            .layer("Persistence").definedBy("..persistence..")

            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
            .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service");

    @ArchTest
    public static final ArchRule controllers_should_be_annotation_restcontroller = ArchRuleDefinition//
            .classes().that().resideInAPackage("br.com.archunit.demo.controller")//
            .should().beAnnotatedWith(RestController.class)//
            .because("controllers should be annotation");//

    /*
     * Teste para verificar se a camada de domínio não depende das camadas de
     * infraestrutura e adaptadores.
     */
    //@ArchTest
    //private final ArchRule domainShouldNotDependOnAdaptersAndInfrastructure = ArchRuleDefinition.noClasses()
    //        .that().resideInAPackage("..domain..")
    //        .should().dependOnClassesThat().resideInAnyPackage("..adapters..", "..infraestructure..");
//
    //@ArchTest
    //private final ArchRule adaptersShouldNotDependOnInfrastructure = ArchRuleDefinition.noClasses()
    //        .that().resideInAPackage("..adapters..")
    //        .should().dependOnClassesThat().resideInAPackage("..infraestructure..");
//
    ///*
    // * Teste para verificar se classes da camada de adapter possui a nomenclatura
    // * correta.
    // */
    //@ArchTest
    //private final ArchRule classesInAdaptersPackageShouldHaveCorrectMethodName = ArchRuleDefinition.classes()
    //        .that().resideInAPackage("..adapters..")
    //        .should().haveNameMatching(".*Adapter");

}