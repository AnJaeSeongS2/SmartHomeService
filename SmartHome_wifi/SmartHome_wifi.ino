#include <WiFi.h>
#include <AES.h>  //데이터 암호화/복호화
#include <SPI.h>

#include <Arduino.h>

char ssid[] = "LKC";     //  연결하실 와이파이 이름
char pass[] = "lkc930216";  // 네트워크 보안키

WiFiClient client;
IPAddress IPserver(192, 168, 0, 22); // Server IP Address

uint8_t data[80];
uint8_t publicClient[64];
uint8_t publicServer[64];
uint8_t publicChip[64];
uint8_t session[32];
uint8_t plain[16] = {16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
uint8_t plain_toServ[16];
uint8_t cypher[16];
uint8_t signatureClient[64];



volatile int i;

AES aes ; //aes256로 데이터 암호화 할 예정



void print_data(uint8_t *_data, int _Size);

void get_data_client(uint8_t *_data, int _Size);
void set_data_client(uint8_t *_data, int _Size);

void get_data_serial(uint8_t *_data, int _Size);
void set_data_serial(uint8_t *_data, int _Size);

void get_signature(uint8_t *p, uint8_t *s);



void setup() {
  // put your setup code here, to run once:
  Serial.begin(57600);

  while ( (WiFi.begin(ssid, pass) != WL_CONNECTED) ) {
    Serial.println("Connectless");
  }
  // 연결에 성공했으면 연결확인 메시지와 연결된 네트워크 정보를 띄운다.
  Serial.println("CN ");
  if (client.connect(IPserver, 2345))
  {
    Serial.println("CS");
  }

  get_data_serial(publicChip, 64);
  set_data_client(publicChip, 64);
  // Chip's public key transport

  get_data_serial(publicClient, 64);
  set_data_client(publicClient, 64);
  // public key transport for ECDH

  get_data_client(publicServer, 64);
  set_data_serial(publicServer, 64);
  // get Server's public key for ECDH

  get_data_serial(session, 32);
  aes.set_key (session, 256);
  //  aes.encrypt(plain, cypher);
  // get session key

  /*
      // make signature
    get_signature(plain, signatureClient);
    memcpy(data, cypher, 16);
    memcpy((data + 16), signatureClient, 64);
    set_data_client(data, 80);

    get_data_client(cypher, 16);
    aes.decrypt(cypher, plain_toServ);
  */
}

void loop() {
  // put your main code here, to run repeatedly
  /*
    Serial.println();
    print_data(publicChip, 64);
    print_data(publicClient, 64);
    print_data(publicServer, 64);
    print_data(session, 32);
    print_data(plain, 16);
    print_data(plain_toServ, 16);
    print_data(cypher, 16);
    print_data(signatureClient, 64);
    delay(100000);
  */
  get_data_client(cypher, 16);
  aes.decrypt(cypher, plain);

  set_data_serial(plain, 16);
  
  get_signature(plain, signatureClient);

  aes.encrypt(plain, cypher);
  memcpy(data, cypher, 16);
  memcpy((data + 16), signatureClient, 64);
  set_data_client(data, 80);
}

void get_data_client(uint8_t *_data, int _Size)
{
  while (!client.available());
  for ( i = 0 ; i < _Size ; i++ )
    _data[i]  = (uint8_t)client.read();
}
void set_data_client(uint8_t *_data, int _Size)
{
  for ( i = 0 ; i < _Size ; i++ )
    client.write((char)_data[i]);
}

void get_data_serial(uint8_t *_data, int _Size)
{
  /*
    while (Serial.available() < size-1);
    for ( i = 0 ; i < size ; i++ )
    data[i]  = (uint8_t)Serial.read();
  */
  i = 0;
  while ( i < _Size )
  {
    if ( Serial.available() > 0 )
    {
      _data[i]  = (uint8_t)Serial.read();
      i++;
    }
    delay(10);
  }
}

void set_data_serial(uint8_t *_data, int _Size)
{
  for ( i = 0 ; i < _Size ; i++ )
    Serial.print((char)_data[i]);
}

void print_data(uint8_t *_data, int _Size)
{
  for ( i = 0 ; i < _Size ; i++ )
  {
    Serial.print(_data[i]);
    Serial.print(' ');
    if ( (i + 1) % 16 == 0 )
      Serial.print('\n');
  }
  Serial.print('\n');
}

void get_signature(uint8_t *p, uint8_t *s)
{
  get_data_serial(p, 16);
  get_data_serial(s, 64);
}
