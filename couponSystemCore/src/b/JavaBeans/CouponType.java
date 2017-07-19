package b.JavaBeans;

/**
 * This file defines the different possibilities for CouponType
 */

public enum CouponType {

	RESTAURANTS, ELECTRICITY, ELECTRONICS, FOOD, HEALTH, SPORTS, CAMPING, TRAVELING, CLOTHS, ENTERTAINMENT,
	CARS, BIKES, MUSEUMS, THEATRE, CHILD_ACTIVITIES, HOMEWARE, FURNITURE, PHARMACEUTICALS, TECHNOLOGY, ART,
	BOOKS, MUSIC, BABY_SUPPLY, BEVERAGES, SHOES, MAKEUP, JEWELRY, HOTELS, NATURE;

	
    public static CouponType fromString(String type) {
        return valueOf(type.toUpperCase());
    }
}