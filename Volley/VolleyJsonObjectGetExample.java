
// get Json object

String url="http://api.openweathermap.org/data/2.5/weather?q=";

private void callForData(String url){

JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlO, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
				
				// TODO
				// get data
				
                    String place = response.getString("name");
					
                    JSONObject mainObject = response.getJSONObject("main");				
                    String temp = mainObject.getString("temp");
                    String pressure = mainObject.getString("pressure");
                    String humidity = mainObject.getString("humidity");
                    String temp_min = mainObject.getString("temp_min");
                    String temp_max = mainObject.getString("temp_max");
					
                    JSONObject windObject = response.getJSONObject("wind");
                    String speed =  windObject.getString("speed");
                    String deg =  windObject.getString("deg");
					
                    JSONObject sysObject = response.getJSONObject("sys");
                    String sunrise = sysObject.getString("sunrise");
                    String sunset = sysObject.getString("sunset");

                    JSONArray descriptionArray  = response.getJSONArray("weather");
                    JSONObject descriptionObject = descriptionArray.getJSONObject(0);
                    String condition = descriptionObject.getString("description");
				
				// Set Data
				
                    tempTV.setText(tempNew+"℃");
                    pressureTV.setText(pressure+" hPa");
                    humidityTV.setText(humidity+" %");
                    windSpeedTV.setText(speed+" meter/sec");
                    placeTV.setText(place);
                    locationTV.setText(location);
                    timeTV.setText(currentDateTimeString);
                    descriptionTV.setText(condition.toUpperCase());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();*/

            }
        });
		
		// Add to requestQueue
		
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);








}