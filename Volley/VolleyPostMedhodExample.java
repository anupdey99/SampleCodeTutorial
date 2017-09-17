

public static final String LOGIN_URL = "http://www.delaroystudios.com/channel/register/login.php";

public static final String KEY_USERNAME="username";
public static final String KEY_PASSWORD="password";


private void userLogin() {
 
 	
		// get Data from user
        username = editTextUsername.getText().toString().trim();  
        password = editTextPassword.getText().toString().trim();

				
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
					
					// TODO
                        if(response.trim().equals("success")){
                            openProfile();
                        }else{
                            Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();
                        }
						
						
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(KEY_USERNAME,username);
                map.put(KEY_PASSWORD,password);
                return map;
            }
        };

		
		// add to requestQueue
		
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
		
		or
		
		AppController.getInstance().addToRequestQueue(stringRequest);

		
}