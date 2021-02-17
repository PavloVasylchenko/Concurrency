# Concurrency task  
Provided file need to be processed via translate method as fast as possible with provided limitations.
Currently it fails because of memory limitation and need to be fixed in pull request.

# Limitations
Application need to consume less memory than provided file.
Translate api allow 4 parallel calls only.

# Run instructions
javac -d out src/**/*.java   
java -Xmx10M -cp out/ me.vasylchenko.Concurrency  

# Expected result  
Time should be almost the same as expected time  
**Time: 128745**  
**Expected: 126889**  
Expected time will be different each run.  
