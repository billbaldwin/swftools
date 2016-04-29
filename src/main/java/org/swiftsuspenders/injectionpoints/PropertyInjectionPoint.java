package org.swiftsuspenders.injectionpoints;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
public class PropertyInjectionPoint extends InjectionPoint {
	public String propertyName;
	public String propertyType;
	public String injectionName;
}
