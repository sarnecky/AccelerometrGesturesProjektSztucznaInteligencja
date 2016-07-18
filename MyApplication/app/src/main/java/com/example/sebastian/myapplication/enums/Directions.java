package com.example.sebastian.myapplication.enums;

public enum Directions {
	LEFT, RIGHT, UP, DOWN, UNKNOWN;

	@Override
	public String toString() {
		if (super.toString() == "UNKNOWN")
			return "";
		return super.toString().toLowerCase();
	}
}
