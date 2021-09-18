int relay1 = 4;
int relay2 = 5;
int relay3 = 6;
int relay4 = 7;
int relay5 = 8;
#include <SoftwareSerial.h>
SoftwareSerial BT(9, 10); //RX, TX respetively
String readdata;
    char c;
void setup() {
    BT.begin(9600);
 Serial.begin(9600);
  pinMode(relay1, OUTPUT);
  pinMode(relay2, OUTPUT);
  pinMode(relay3, OUTPUT);
  pinMode(relay4, OUTPUT);
  pinMode(relay5, OUTPUT);
  digitalWrite(relay1,HIGH);
  digitalWrite(relay2,HIGH);
  digitalWrite(relay3,HIGH);
  digitalWrite(relay4,HIGH);
  digitalWrite(relay5,HIGH);
    readdata.reserve(200);
}

void loop() {


  while (BT.available()) { //Check if there is an available byte to read

 
    c = BT.read(); //Conduct a serial read
  //  readdata += c; 
Serial.println(c);

    if(c=='a') digitalWrite(relay1,LOW);
    if(c=='b') digitalWrite(relay1,HIGH);
    if(c=='c') digitalWrite(relay2,LOW);
    if(c=='d') digitalWrite(relay2,HIGH);
    if(c=='e') digitalWrite(relay3,LOW);
    if(c=='f') digitalWrite(relay3,HIGH);
    if(c=='g') digitalWrite(relay4,LOW);
    if(c=='h') digitalWrite(relay4,HIGH);
    if(c=='i') digitalWrite(relay5,LOW);
    if(c=='j') digitalWrite(relay5,HIGH);
    

  }c="";
}
