# Google Places API (Place Finder) - Android

App show nearby bars within a proximity of two kilometers both on a map and in a list. App requests location update only once, at the beginging using [FusedLocationProviderApi](https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderApi)

We are using a [third party](https://github.com/nomanr/Android-Google-Places-API) library with a slight modification to include place pictures. We are using two different set of API keys for Google Maps and Places API since there's a limit to Places API requests. You can replace your own API keys in the Manifest file.
