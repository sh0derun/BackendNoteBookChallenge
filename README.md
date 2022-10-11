# BackendNoteBookChallenge

A simple notebook server that can execute pieces of python code in an interpreter using Spring Boot to expose API endpoints and Antler to parse code, it can interpret just expression statement, function and class definitions. All previous execuded python code gets saved with sessionId that was executed with, and if there is any error in the stored code, it will be shown in the output object.

this project doesn't contain any tests but it might has some in the future. Feel free to add test throught pull request

It can be tested with any rest client

Endpoints:
- Post /execute
   - Request object (Example - first request):  
   ```json
   {
      "code" : "print(\"Hello World\")", 
      "sessionId": "8upWRlfWL0WSJYoDqsjDIg=="
   }
   ```
   - Response object (Example):
   ```json
   {
      "result" : "Hello World", 
      "code": "", 
      "error": ""
   }
   ```
   - Request object (Example - second request):  
   ```json
   {
      "code" : "2**3", 
      "sessionId": "8upWRlfWL0WSJYoDqsjDIg=="
   }
   ```
   - Response object (Example):
   ```json
   {
      "result" : "Hello World\n 8", 
      "code": "print(\"Hello World\")", 
      "error": ""
   }
   ```
