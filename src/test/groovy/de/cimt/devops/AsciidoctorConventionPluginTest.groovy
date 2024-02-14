package de.cimt.devops;

import org.gradle.testkit.runner.TaskOutcome

class AsciidoctorConventionPluginTest extends PluginTest {

	def setup() {
		buildFile << """
            plugins {
                id 'de.cimt.asciidoctor-conventions'
            }
        """
	}

	def "publishes document with versioning"() {
		given:
		settingsFile.setText("rootProject.name = 'my-doc'")
		buildFile << """
            version = '0.1.0'

            publishing {
                repositories {
                    maven {
                        name 'testRepo'
                        url 'build/test-repo'
                    }
                }
            }
        """

		new File(testProjectDir, 'src/docs/asciidoc').mkdirs()
		new File(testProjectDir, 'src/docs/asciidoc/my-doc.adoc') << """
		= Beispieldokument
		:author: Autor
		:email: <autor@example.com>
		:doctype: article
		:sectnums:

		Beispieldokument
        """

		when:
		def result = runTask('asciidoctorPdf')

		then:
		result.task(":asciidoctorPdf").outcome == TaskOutcome.SUCCESS
		//result.task(":publish").outcome == TaskOutcome.SUCCESS
		new File(testProjectDir, 'build/docs/asciidocPdf').exists()
	}
}
