package com.example.qrconnect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the AvatarGenerator class
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AvatarGeneratorTest {

    /**
     * Creates a mock UserProfile to generate an avatar for
     * @param firstName String, user's first name
     * @param lastName String, user's first name
     * @param userID String, user's ID
     * @return UserProfile
     */
    private UserProfile mockUser(String firstName, String lastName, String userID) {
        return new UserProfile(userID, firstName, lastName);
    }

    /**
     * Tests if the generated avatars will be different for all users, even if they have the same name
     */
    @Test
    public void testDifferentUsers() {
        UserProfile user1 = mockUser("John", "Doe", "1");
        UserProfile user2 = mockUser("John", "Doe", "2");
        Bitmap avatar1 = AvatarGenerator.generateAvatar(user1);
        Bitmap avatar2 = AvatarGenerator.generateAvatar(user2);
        assertFalse(avatar1.sameAs(avatar2));
    }

    /**
     * Tests if the generated avatar is deterministic
     */
    @Test
    public void testSameUser() {
        UserProfile user = mockUser("John", "Doe", "1");
        Bitmap avatar1 = AvatarGenerator.generateAvatar(user);
        Bitmap avatar2 = AvatarGenerator.generateAvatar(user);
        assertTrue(avatar1.sameAs(avatar2));
    }

    /**
     * Tests that avatar will still be generated for a user without a name inputted
     */
    @Test
    public void testNoNamedUser() {
        UserProfile user = mockUser(null, null, "1");
        Bitmap avatar = AvatarGenerator.generateAvatar(user);
        assertNotNull(avatar);
    }
}
