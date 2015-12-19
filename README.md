<div>
	<h4>Environment</h4>
	<li>Tomcat8.0+
	<li>Java8.
	<li>Postgre RDBMS 9.4
</di>

<div>
	<h4>Migrating task 1 to Java8</h4>
	<li>Replaced comparator classes with lambda. 
	<li>Implemented parallel calculation function on commands Length and Frequency. Testing results - 15-20% faster (4000lines text Post Office by Charles Bukowski, intel i5 notebook processor.)
	<li>There is no profit in paralleling duplication command, imo.
</div>