package org.swiftsuspenders.injectionpoints;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
public class PostConstructInjectionPoint extends InjectionPoint {
	public String methodName;
	public int order;
}
