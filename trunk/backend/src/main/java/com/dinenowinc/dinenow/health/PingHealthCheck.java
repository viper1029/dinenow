package com.dinenowinc.dinenow.health;

import lombok.AllArgsConstructor;

import com.codahale.metrics.health.HealthCheck;
import com.dinenowinc.dinenow.resources.PingResource;

@AllArgsConstructor
public class PingHealthCheck extends HealthCheck{
	private PingResource pingResource;
	
	@Override
	protected Result check() throws Exception {
		String s = pingResource.pongAuthenticated(12l);
		if (s.contains("12")) {
			return Result.healthy();
		}
		return Result.unhealthy("Auth ping should contain user id");
	}

	


}
