## **INSTALLATION**

Requirements - needs sbt to be installed.
How to run - execute run.sh to startup an api node and two backend nodes or run corresponding main classes from IDE of choice.
Ports required to be available - 2551-2553 & 8080 (change in startup params if needed).

## **API**

POST ${host}:8080/index - index a document

example body:

{
	"key": "k1",
	"document": "v1 v2 v3"
}

POST ${host}:8080/search - search documents by term

example body:

{
	"term": {
		"value": "v1"
	}
}

GET ${host}:8080/retrieve/${documentKey} - get document by key