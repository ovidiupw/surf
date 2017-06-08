window.onerror = function(msg, url, line, col, error) {
  console.log("Global window error intercepted!");
  console.log("Error message: " + msg);
  console.log("Error url: " + url);
  console.log("Error line: " + line);
  console.log("Error: " + error);

  var body = document.body;
  body.innerText = 'There was an error while trying to load the application!\n';
  body.innerText += 'Error message: \'' + msg + '\'\n';
  body.innerText += 'Please check your internet connection.';

  document.body = body;
};
