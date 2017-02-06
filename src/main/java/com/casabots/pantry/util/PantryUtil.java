/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.util;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Properties;

import static com.casabots.pantry.util.Constants.PropertyKeys.IMAGE_EXTENSIONS;
import static com.casabots.pantry.util.Constants.PropertyKeys.INGREDIENT_CATEGORIES;
import static com.casabots.pantry.util.Constants.PropertyKeys.STRING_MAX_LENGTH;

@Service
public class PantryUtil {

    private static int stringMaxLength;
    private static String[] imageExtensions;
    private static String[] categories;
    @Resource(name = "configProperties")
    private Properties configuration;

    private PantryUtil() {}

    @PostConstruct
    public void setup() {
        stringMaxLength = Integer.parseInt(configuration.getProperty(STRING_MAX_LENGTH));
        imageExtensions = configuration.getProperty(IMAGE_EXTENSIONS).split(",");
        categories = configuration.getProperty(INGREDIENT_CATEGORIES).split(",");
    }

    public static void verifyStringLength(String property) {
        if (property.length() > stringMaxLength) {
            throw new IllegalArgumentException(property + " exceeds " + stringMaxLength + " characters!!");
        }
    }

    public static void verifyStringsLength(String[] properties) {
        for (String p :
                properties) {
            verifyStringLength(p);
        }
    }

    public static void verifyImageUrl(String url) {
        String extension = "";
        int i = url.lastIndexOf('.');
        if (i > 0) {
            extension = url.substring(i+1);
        }

        if (!Arrays.asList(imageExtensions).contains(extension)) {
            String msg = url + " does not have a valid image extension! Allowed extensions are ";
            for (String ext :
                    imageExtensions) {
                msg = msg + ext + " ";
            }
            throw new IllegalArgumentException(msg);
        }
    }

    public static void verifyIngredientCategory(String category) {
        if (!Arrays.asList(categories).contains(category)) {
            String msg = category + " is not valid! Allowed categories are ";
            for (String cat :
                    categories) {
                msg = msg + cat + " ";
            }
            throw new IllegalArgumentException(msg);
        }
    }
}
