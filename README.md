# NetworkingME
**Networking ME** is a networking library for Java ME platform. It's built on top of Generic Connection Framework and other foundation technologies. It has a modular architecture with well-designed, feature-rich APIs that are easy to use.

## How to Get Started

* Download [latest version](https://java.net/projects/networkingme/downloads/directory/Library) package.
* Add the library jar file (networkingme-vX.jar) to your project's libraries.
* Read this Wiki page, Javadoc, study the example usages below and [samples](https://java.net/projects/networkingme/downloads/directory/Samples).
* Any doubt, post a message in the [Forum](https://java.net/projects/networkingme/forums).

## Requirements

* MIDP 2.0 / CLDC 1.0

## License

Networking ME is available under the [**MIT**](http://opensource.org/licenses/MIT) license.

## Update History

See below a list of all updates and what they consist of:

Version | Date | Contents
------- | ---- | --------
1.1 | 11/19/2013 | 1. Listener classes for CSV, XML JSON and LWUIT Image content formats.<br/> 2. URLLabel and URLButton LWUIT components.
1.0 | 07/12/2013 | 1. Initial release.

## Components Overview

See below a list of all main components and what they are for:

Component | Description
--------- | -----------
HttpRequest | This is the class used to create HTTP request.
HttpResponse | This is the class that represents a response from a HTTP request.
RequestOperation | This is the class responsible for running a request asynchronously and delivery the response.
RequestOperation.Listener | This is the interface that represents a listener to events that are triggered during a request execution.
TextListener | This is the listener class to request events that has a utility method to easy delivery the request's result as string.
BinaryListener | This is the listener class to request events that has a utility method to easy delivery the request's result as bytes.
ImageListener | This is the listener class to request events that has a utility method to easy delivery the request's result as a LCDUI image.
CSVListener | This is the listener class to request events that has a utility method to easy delivery the request's result as CSV.
SAXListener | This is the listener class to request events that has a utility method to easy delivery the request's result as SAX XML handler.
JSONListener | This is the listener class to request events that has a utility method to easy delivery the request's result as JSON object.
ImageListener (LWUIT) | This is the listener class to request events that has a utility method to easy delivery the request's result as a LWUIT image.
HttpClient | This is the class that implements the common patterns of communicating with an web application over HTTP.
URLImageItem | This is a custom item class that loads an image from an URL and displays it in a LCDUI Form.
URLLabel | This is a LWUIT custom Label class that loads an image from an URL and displays it in a LWUIT Form.
URLButton | This is a LWUIT custom Button class that loads an image from an URL and displays it in a LWUIT Form.

## Example Usage

See below a list of some code snippets representing the most common usages:

* **Basic HTTP Request**

```java
...
URL url = new URL("http://www.emobtech.com");
HttpRequest req = new HttpRequest(url);
//
RequestOperation oper = new RequestOperation(req);
oper.start(new TextListener() {
	public void onText(String text) {
		System.out.println("onText: " + text);
	}
	public void onFailure(Request request, RequestException exception) {
		System.out.println("onFailure: " + exception.getMessage());
	}
});
...
```

* **Download an Image (LCDUI and LWUIT)**

```java
...
URL url = new URL("http://www.emobtech.com/images/logo.png");
HttpRequest req = new HttpRequest(url);
//
RequestOperation oper = new RequestOperation(req);
oper.start(new ImageListener() {
	public void onImage(Image image) {
		form.append(image);
	}
	...
});
...
```

* **Download a JSON Format Content**

```java
...
URL url = new URL("http://www.emobtech.com/json/file.json");
HttpRequest req = new HttpRequest(url);
//
RequestOperation oper = new RequestOperation(req);
oper.start(new JSONListener() {
	public void onJSON(JSONObject jsonObject) {
		String value = jsonObject.get("key");
	}
	...
});
...
```

* **Download a XML Format Content**

```java
...
URL url = new URL("http://www.emobtech.com/xml/file.xml");
HttpRequest req = new HttpRequest(url);
//
RequestOperation oper = new RequestOperation(req);
oper.start(new SAXListener(new UserHandler()) {
	public void onSAX(DefaultHandler handler) {
		User[] users = ((UserHandler)handler).getParsedUsers();
	}
	...
});
...
```

* **Download a CSV Format Content**

```java
...
URL url = new URL("http://www.emobtech.com/csv/file.csv");
HttpRequest req = new HttpRequest(url);
//
RequestOperation oper = new RequestOperation(req);
oper.start(new CSVListener() {
	public void onCSV(String[][] csv) {
		String name = csv[0][1];
		String email = csv[0][2];
	}
	...
});
...
```

* **POST HTTP Request**

```java
...
URL url = new URL("http://www.emobtech.com/post.php");
HttpRequest req = new HttpRequest(url, HttpRequest.Method.POST);
//
WebFormBody form = new WebFormBody();
form.addField("name", "eMob Tech");
form.addField("twitter", "@emobtech");
//
req.setBody(body);
//
RequestOperation oper = new RequestOperation(req);
oper.start(...);
...
```

* **POST Binary Data**

```java
...
URL url = new URL("http://www.emobtech.com/post_file.php");
HttpRequest req = new HttpRequest(url, HttpRequest.Method.POST);
//
MultipartBody form = new MultipartBody();
form.addPart("file", "notes.txt", "text/plain", "Networking ME library rocks!!!".getBytes());
//
req.setBody(body);
//
RequestOperation oper = new RequestOperation(req);
oper.start(...);
...
```

* **Header and Cookies**

```java
...
HttpRequest req = new HttpRequest(url);
req.setHeader(HttpRequest.Header.USER_AGENT, "MyApp/1.0 (JavaME; MIDP2; CLDC1)");
req.addCookie(new Cookie("name", "value"));
...
```

* **Access REST services with HttpClient**

```java
...
URL urlBase = new URL("http://www.emobtech.com/rest");
HttpClient httpClient = new HttpClient(url);
//
httpClient.get("/friends.json", new TextListener() {
	public void onText(String text) {
		System.out.println("JSON: " + text);
	}
	...
});
...
```

* **HttpClient to Post Data**

```java
...
URL urlBase = new URL("http://www.emobtech.com/rest");
HttpClient httpClient = new HttpClient(url);
//
Hashtable params = new Hashtable();
params.put("first_name", "Ernandes");
params.put("email", "ernandes@emobtech.com");
params.put("twitter", "ernandesmjr");
//
httpClient.postForm("/new_friend.json", params, new TextListener() {
	...
});
...
```

* **HttpClient to Upload File**

```java
...
URL urlBase = new URL("http://www.emobtech.com/rest");
HttpClient httpClient = new HttpClient(url);
//
bytes[] pictureBytes = ...;
//
httpClient.uploadFile(
	"/set_friend_picture.json",
	"picture",
	"picture.png",
	"image/png",
	pictureBytes,
	new TextListener() {
	...
});
...
```

* **Http Basic Access Authentication with HttpClient**

```java
...
URL urlBase = new URL("http://www.emobtech.com/private/file.txt");
HttpClient httpClient = new HttpClient(url);
//
httpClient.setBasicAuthentication("username", "password");
...
```

* **Display an Image in a Form from an URL**

```java
...
URL url = new URL("http://www.emobtech.com/images/logo.png");
URLImageItem imageItem = new URLImageItem("Image", url, Item.LAYOUT_CENTER, imagePlaceholder, "at text");
//
Form form = new Form("URL Image");
form.append(imageItem);
...
```

* **Display an Image inside a Label in a LWUIT Form from an URL**

```java
...
URL url = new URL("http://www.emobtech.com/images/logo.png");
URLLabel imageLabel = new URLLabel(url);
//
Form form = new Form("URL Image");
form.addComponent(imageLabel);
...
```

* **Display an Image inside a Button in a LWUIT Form from an URL**

```java
...
URL url = new URL("http://www.emobtech.com/images/logo.png");
URLButton imageButton = new URLButton(url);
//
Form form = new Form("URL Image");
form.addComponent(imageButton);
...
```

## Credits

This project was created by [Ernandes Jr](http://linkedin.com/in/ernandes/). ([@ernandesmjr](http://www.twitter.com/ernandesmjr)), founder at [eMob Tech](http://www.emobtech.com) ([@emobtech](http://www.twitter.com/emobtech)).

## References

* [Hypertext Transfer Protocol](http://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol)
* [HTTP Request Methods](https://en.wikipedia.org/wiki/HTTP_method#Request_methods)
* [HTTP Header Fields](https://en.wikipedia.org/wiki/List_of_HTTP_header_fields)
* [HTTP Response Codes](https://en.wikipedia.org/wiki/HTTP_response_codes)
* [Basic Access Authentication](http://en.wikipedia.org/wiki/Basic_access_authentication)
* [HTTP POST](http://en.wikipedia.org/wiki/POST_(HTTP))
* [Multipart Messages](http://en.wikipedia.org/wiki/Multipart/form-data#Multipart_messages)
* [Payload](http://en.wikipedia.org/wiki/Payload_(computing))
* [URL](http://en.wikipedia.org/wiki/Uniform_resource_locator)
* [Cookie](https://en.wikipedia.org/wiki/HTTP_cookie)
* [Java Micro Edition](http://java.sun.com/javame/index.jsp)

## External Links
* [eMob Tech](http://www.emobtech.com)
* [J2ME Group Blog](http://j2megroup.blogspot.com)
