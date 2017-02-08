/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.samstudy.util;

public final class Constants {

    public static final class Roles {

        public static final String USER_ROLE = "user";
        public static final String OWNER_ROLE = "owner";
        public static final String SALLY_ROLE = "sally";
        public static final String ADMIN_ROLE = "admin";

        private Roles() {
        }
    }

    public static final class OrderStatus {

        public static final String PROGRESS = "pending";
        public static final String FAILED = "failed";
        public static final String SUCCESS = "success";

        private OrderStatus() {
        }
    }

    public static final class UserLoginType {

        public static final String FBUSER = "facebook user";
        public static final String GOOGLEUSER = "google user";

        private UserLoginType() {}
    }

    public static final class PropertyKeys {

        public static final String APP_ID = "fb-app-id";
        public static final String APP_SECRET = "fb-app-secret";
        public static final String FB_REDIRECT_URI = "fb-redirect-uri";
        public static final String CLIENT_ID = "google-client-id";

        // String max length
        public static final String STRING_MAX_LENGTH = "string.maxlength";
        // Allowed image extensions
        public static final String IMAGE_EXTENSIONS = "image.extensions";
        // Categories of ingredients
        public static final String INGREDIENT_CATEGORIES = "ingredient.categories";

        private PropertyKeys() {
        }
    }

    public static final class DistanceUnits {

        public static final String KM = "kilo meters";
        public static final String MILES = "miles";
        public static final String NM = "nautical miles";

        private DistanceUnits() {
        }
    }

    private Constants() {
    }
}
