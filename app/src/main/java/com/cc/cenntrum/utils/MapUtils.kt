package com.cc.cenntrum.utils

import android.content.Context
import android.graphics.Color
import android.location.Geocoder
import android.util.Log
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Math.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


object MapUtils {

    var isMapRunning = false


     var originLatitude: Double = 31.582045
     var originLongitude: Double = 74.329376
    // Coordinates of a park nearby
     var destinationLatitude: Double = 28.5151087
     var destinationLongitude: Double = 77.3932163



    // Create a function to generate the direction URL
    fun getDirectionURL(origin: LatLng, dest: LatLng, secret: String): String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }

    suspend fun getDirection(url: String,tv:TextView ,mMap: GoogleMap) {
        val result = withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()

            val result = ArrayList<List<LatLng>>()
            try {
                val respObj = Gson().fromJson(data, MapParameters.MapData::class.java)
                val path = ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size) {
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return@withContext result
        }
        val lineoption = PolylineOptions()
        for (i in result.indices) {
            lineoption.addAll(result[i])
            lineoption.width(10f)
            lineoption.color(Color.GREEN)
            lineoption.geodesic(true)
        }
        mMap.addPolyline(lineoption)
        tv.text = "${getKilometers(originLatitude, originLongitude, destinationLatitude,
            destinationLongitude)} Km"
    }

    //  Create a function to decode polyline
    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }


    fun addMarker(latitude: Double, longitude: Double,mMap: GoogleMap,context: Context) {
        try {
            //  binding.idSearchView.setQuery(singleton.getGoogleAddress(), false)
            // on below line we are creating a variable for our location
            // where we will add our locations latitude and longitude.
            val latLng = LatLng(latitude, longitude)

            // on below line we are adding marker to that position.
            mMap.addMarker(
                MarkerOptions().position(latLng).title(
                    getAddressFromLatitude(context, latitude, longitude)
                )
            )

            // below line is to animate camera to that position.
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        } catch (e: java.lang.Exception) {
        }

    }

    fun addMarker(latLng: LatLng ,address: String, mMap: GoogleMap) {
        try {
            //  binding.idSearchView.setQuery(singleton.getGoogleAddress(), false)
            // on below line we are creating a variable for our location
            // where we will add our locations latitude and longitude.

            // on below line we are adding marker to that position.
            mMap.addMarker(
                MarkerOptions().position(latLng).title(
                   address
                )
            )

            // below line is to animate camera to that position.
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        } catch (e: java.lang.Exception) {
        }
    }


    fun getAddressFromLatitude(context: Context, latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude,
                longitude,
                1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            val address =
                addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            //            String city = addresses.get(0).getLocality();
//            String state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String postalCode = addresses.get(0).getPostalCode();
//            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            return address
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }





    fun addMarkerOnTouchMap(mMap: GoogleMap) {
        var markerPoints = mutableListOf<LatLng>()
        val lahore = LatLng(31.582045, 74.329376)
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lahore, 16f))
        mMap.setOnMapClickListener { latLng ->
            if (markerPoints.size > 1) {
                markerPoints.clear()
                mMap.clear()
            }

            // Adding new item to the ArrayList
            markerPoints.add(latLng)

            // Creating MarkerOptions
            val options = MarkerOptions()

            // Setting the position of the marker
            options.position(latLng)
            if (markerPoints.size == 1) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            } else if (markerPoints.size == 2) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            }

            // Add new marker to the Google Map Android API V2
            mMap.addMarker(options)

        }
    }


    fun getKilometers(lat1: Double, long1: Double, lat2: Double, long2: Double): Double {
        val PI_RAD = Math.PI / 180.0
        val phi1 = lat1 * PI_RAD
        val phi2 = lat2 * PI_RAD
        val lam1 = long1 * PI_RAD
        val lam2 = long2 * PI_RAD
         val kilometers = 6372.08 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1))
        return Math.round(kilometers).toDouble()
    }




//    private fun getDistanceOnRoad(
//        latitude: Double, longitude: Double,
//        prelatitute: Double, prelongitude: Double
//    ): String? {
//        var result_in_kms: String? = ""
//        val url = ("http://maps.google.com/maps/api/directions/xml?origin="
//                + latitude + "," + longitude + "&destination=" + prelatitute
//                + "," + prelongitude + "&sensor=false&units=metric")
//        val tag = arrayOf("text")
//        var response: HttpResponse? = null
//        try {
//            val httpClient: HttpClient = DefaultHttpClient()
//            val localContext: HttpContext = BasicHttpContext()
//            val httpPost = HttpPost(url)
//            response = httpClient.execute(httpPost!!, localContext!!)
//            val `is`: InputStream = response.getEntity().getContent()
//            val builder: DocumentBuilder = DocumentBuilderFactory.newInstance()
//                .newDocumentBuilder()
//            val doc: Document = builder.parse(`is`)
//            if (doc != null) {
//                var nl: NodeList
//                val args = ArrayList<Any>()
//                for (s in tag) {
//                    nl = doc.getElementsByTagName(s)
//                    if (nl.getLength() > 0) {
//                        val node: Node = nl.item(nl.getLength() - 1)
//                        args.add(node.getTextContent())
//                    } else {
//                        args.add(" - ")
//                    }
//                }
//                result_in_kms = java.lang.String.format("%s", args[0])
//            }
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//        return result_in_kms
//    }

































    // Second method to draw  rout between two point just pass url of two points
    @Throws(IOException::class)
    fun downloadUrl(strUrl: String): String? {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            iStream = urlConnection.inputStream
            val br = BufferedReader(InputStreamReader(iStream))
            val sb = StringBuffer()
            var line: String? = ""
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            data = sb.toString()
            br.close()
        } catch (e: java.lang.Exception) {
            Log.d("Exception", e.toString())
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

}