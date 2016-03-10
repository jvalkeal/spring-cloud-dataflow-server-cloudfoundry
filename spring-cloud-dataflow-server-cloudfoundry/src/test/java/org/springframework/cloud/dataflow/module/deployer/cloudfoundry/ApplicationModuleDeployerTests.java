/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.dataflow.module.deployer.cloudfoundry;

import org.junit.Before;
import org.junit.ClassRule;

import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cloud.dataflow.module.deployer.test.AbstractModuleDeployerTests;

/**
 * Runs integration tests for {@link ApplicationModuleDeployer}, using the production configuration,
 * that may be configured via {@link CloudFoundryModuleDeployerProperties}.
 *
 * Tests are only run if a successful connection can be made at startup.
 *
 * @author Eric Bottard
 */
@SpringApplicationConfiguration(classes = CloudFoundryModuleDeployerConfiguration.class)
@IntegrationTest
public class ApplicationModuleDeployerTests extends AbstractModuleDeployerTests {


	/**
	 * Execution environments may override this default value to have tests wait longer for a deployment, for example if
	 * running in an environment that is known to be slow.
	 */
	protected double timeoutMultiplier = 1.0D;

	@Before
	public void init() {
		String multiplier = System.getenv("CF_DEPLOYER_TIMEOUT_MULTIPLIER");
		if (multiplier != null) {
			timeoutMultiplier = Double.parseDouble(multiplier);
		}
	}


	/**
	 * Return the timeout to use for repeatedly querying a module while it is being deployed.
	 * Default value is one minute, being queried every 5 seconds.
	 */
	protected Attempts deploymentTimeout() {
		return new Attempts(12, (int) (5000 * timeoutMultiplier));
	}

	/**
	 * Return the timeout to use for repeatedly querying a module while it is being un-deployed.
	 * Default value is one minute, being queried every 5 seconds.
	 */
	protected Attempts undeploymentTimeout() {
		return new Attempts(20, (int) (5000 * timeoutMultiplier));
	}




	@ClassRule
	public static CloudFoundryTestSupport cfAvailable = new CloudFoundryTestSupport();

}