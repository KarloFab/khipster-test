package com.kfabija.test.cucumber

import com.kfabija.test.IntegrationTest
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration

@CucumberContextConfiguration
@IntegrationTest
@WebAppConfiguration
class CucumberTestContextConfiguration
