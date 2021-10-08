# Connect6_GUI_Server


### Getting Started

1. Clone this CONNSIX repository.

    ```bash
    $ git clone https://github.com/ARISE-Handong/CONNSIX.git
    ```

2. Install Maven in [Apache Maven Project Site](https://maven.apache.org/download.cgi#)

    - To check whether Maven is installed well, enter a following command in a terminal
  
        ```bash
        $ mvn -version
        ```

    - If Apache Maven Version information is displayed, you succeess to install Maven! 
    
        * Examples
    
            + Mac OS
    
                ```bash
                Apache Maven 3.8.3 (ff8e977a158738155dc465c6a97ffaf31982d739)
                Maven home: /usr/local/Cellar/maven/3.8.3/libexec
                Java version: 17, vendor: Homebrew, runtime: /usr/local/Cellar/openjdk/17/libexec/openjdk.jdk/Contents/Home
                Default locale: ko_KR, platform encoding: UTF-8
                OS name: "mac os x", version: "11.5.2", arch: "x86_64", family: "mac"
                ```
    
            + Windows
    
                ```bash
                Apache Maven 3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f)
                Maven home: C:\Users\PC\Downloads\apache-maven-3.6.3-bin\apache-maven-3.6.3\bin\..
                Java version: 11.0.8, vendor: Amazon.com Inc., runtime: C:\Program Files\Amazon Corretto\jdk11.0.8_10
                Default locale: ko_KR, platform encoding: MS949
                OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
                ```

3. Enter following commands for compilation and generation of jar file.

    ```bash
    $ cd single-mode
    $ mvn compile
    $ mvn package
    ```
  
4. Enter a following command for execution.

    ```bash
    $ java -cp target/CONNSIX-1.0.0.jar Main
    ```
  
5. Then, a CONNSIX window appears, You can start a game!



  
  
   
 
    
