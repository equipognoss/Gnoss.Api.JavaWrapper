package org.gnoss.apiWrapper.Helpers;

/**
 * Administration page types enumeration
 * @author GNOSS
 */
public enum AdministrationPageType {

	/**
	 * Design administration page type (value: 0)
	 */
	Design(0),
	
	/**
	 * Page administration page type (value: 1)
	 */
	Page(1),
	
	/**
	 * Semantic administration page type (value: 2)
	 */
	Semantic(2),
	
	/**
	 * Thesaurus administration page type (value: 3)
	 */
	Thesaurus(3),
	
	/**
	 * Text administration page type (value: 4)
	 */
	Text(4);

	private final int value;

	/**
	 * Constructor for AdministrationPageType
	 * @param value The integer value associated with the enum constant
	 */
	AdministrationPageType(int value) {
		this.value = value;
	}
	
	/**
	 * Gets the integer value associated with this enum constant
	 * @return The integer value
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * Gets an AdministrationPageType enum constant from its integer value
	 * @param value The integer value to convert
	 * @return The corresponding AdministrationPageType enum constant
	 * @throws IllegalArgumentException if no enum constant matches the given value
	 */
	public static AdministrationPageType fromValue(int value) {
		for (AdministrationPageType type : AdministrationPageType.values()) {
			if (type.value == value) {
				return type;
			}
		}
		throw new IllegalArgumentException("No AdministrationPageType with value " + value);
	}
	
	/**
	 * Returns a string representation of this enum constant
	 * @return The name and value of the enum constant
	 */
	@Override
	public String toString() {
		return name() + "(" + value + ")";
	}
}