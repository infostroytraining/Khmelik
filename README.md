<h3>Task2</h3>

<div>
	<h4>Environment</h4>
	<li>Tomcat8.0+
	<li>Java8.
</di>

<div>
	<h4>Validation</h4>
	<li>Implemented simple field-validation, that checks whether input value matches some regex pattern. 
	<li>Implemented DTO temp data saving.
</div>

<div>
	<h4>User avatar saving</h4>
	<li>While using Tomcat servlet-container, creates an "../webapps/images/" directory to save user images.
	<li>After successfull registration user is redirected to welcome-page, where he can see his avatar uploaded.
</div>

<div>
	<h4>CAPTCHA</h4>
	<li>Implemented Google reCAPTCHA method. Uses public-key cryptography (private key can be available only at server-side of application). Also uses Google validation API.
	<li>Implemented SimpleCaptcha method. Generates an image of random symbols with some visual disturbance. Client-side receives an image, called "simpleCapthca.png". The answer string is stored in sessionScope on server-side.
	<li>It is also possible to refresh SimpleCaptcha without reloading of the registraion page.
</div>