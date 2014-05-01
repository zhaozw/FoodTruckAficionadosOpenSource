# This is my README for the Google Play Store app, Food Truck Aficionados Free

I've been asked about the name of my app. I'm a fan of irony.

This is open-source code I've made available for the app I have in the play store at https://play.google.com/store/apps/details?id=com.justin.freetruckfinder.app


SYNOPSIS:
Just few components that were fun to implement to ensure my app is ALIVE when in your hands, no matter what direction you are pointing in, the compass points at the location of the moving target (food truck/stand):
I thoroughly enjoyed using my own formula for calculating distance because it was more fun. Utilizing transformation matrices to allow for all rotations (0, 90, 180, 270) of a device while also having the app respect the native orientation of the Android device. Why? Some tablets (no idea how many or how few) have native landscape orientation for their coordinate system and I didn't care to know which ones they are since the coordinate system tranformations make them work for all tablets and smartphones. I also had a lot of fun doing the necessary math that used two sensors that are present in almost every device capable/running API level 14. I could technically go lower and choose not to. Also, the developer console tells me it's compatible with all existing 3,000+ devices that can access the Google Play Store. For a LIVE user experience, the compass pointing to (mag) north and the calculations necessary to offset the arrow pointing to the food truck regardless of the phone's orientation. The custom interpolation that doesn't suffer from Gimble Lock (Euler angle flip, coordinate singularity, etc.) for a smoooooooootheeee and USER-INTERACTIVE compass & pointer to each food truck/stand. Keep in mind, a lot of food trucks/stands are moving targets. Hold your Android device and spin around, you'll see that, "IT'S ALIVE!!! ALIVE!!!" and the pointers will point at each individual food truck/stand whichever way you turn. Did I mention that it points at the food truck/stand whichever way your turn? LITERALLY take it for a spin. 


SHARING IS CARING!
I want to share this code so that everyone can have access to any components they may find of use to them.

Keep in mind the FourSquare clientID and clientSecret are no longer valid, and the Google Places API key is no longer valid either. You'll need to provide your own if you utilize my code.


VERSION:
This version is some days prior to the first version I published. It's almost exactly the same project except that the one I use for the Play Store has code that is better organized for debugging and has some optimizations for performance. For version control reasons, I'm not able to include that version, but I'll enjoy manually re-factoring my code once again sometime in the next couple weeks...see below


UPDATE:
RE-FACTORED CLEAN CODE COMING IN THE NEXT FEW WEEKS...just need a break from the app's code that I worked through for more than a month, I think almost 2 - a bit of a blur hahaha. It'll be far better after taking a break from the app and working on other stuff...UNLESS I get feedback/reviews before then that indicate bugs/issues that will actually provide me with some fun times debugging/fixing.
