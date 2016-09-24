#include <uECC.h> //micro-ecc라이브러리, ecdh로 session키를 만든다.
//#include <Servo.h>
#include <cryptoauth.h>

static int RNG(uint8_t *dest, unsigned size);

#define USE_ATECC508      // for ECC Chip
#define ledPin 11
#define greenLedPin 10
#define redLedPin 5

//#define servoPin 5
//AES aes ; //aes256로 데이터 암호화 할 예정

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
uint8_t privateClient[32];
uint8_t publicClient[64];
uint8_t publicServer[64];
uint8_t publicChip[64];
uint8_t session[32];
uint8_t signatureClient[64];

uint8_t cypher[16];
uint8_t plain[16];

const struct uECC_Curve_t * curve = uECC_secp256r1();

int light = 0;  // for light
int greenlight = 0;  // for light
int bright = 0;  // for boiler

//Servo servo;  // for using servo motor

volatile int i;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void onoffLight()
{
  if ( light == 0 )
  {
    for ( i = 0 ; i < 256 ; i++ )
    {
      analogWrite(ledPin, i);
      delay(10);
    }
    light = 255;
  }
  else
  {
    for ( i = 0 ; i < 256 ; i++ )
    {
      analogWrite(ledPin, 255 - i);
      delay(10);
    }
    light = 0;
  }
}
void onoffGreenLight()
{
  if ( greenlight == 0 )
  {
    for ( i = 0 ; i < 256 ; i++ )
    {
      analogWrite(greenLedPin, i);
      delay(10);
    }
    greenlight = 255;
  }
  else
  {
    for ( i = 0 ; i < 256 ; i++ )
    {
      analogWrite(greenLedPin, 255 - i);
      delay(10);
    }
    greenlight = 0;
  }
}
void onoffBoiler(int b)
{
  if ( bright > b )
  {
    for ( i = bright ; i >= b ; i-- )
    {
      analogWrite(redLedPin, i);
      delay(10);
    }
  }
  else
  {
    for ( i = bright ; i <= b ; i++ )
    {
      analogWrite(redLedPin, i);
      delay(10);
    }
  }
  bright = b;
}

/*
  void onoffWindow()
  {
  if ( angle == 0 )
  {
    for ( angle = 0 ; angle < 90 ; angle++ )
    {
      servo.write(angle);
      delay(30);
    }
  }
  else
  {
    for ( angle = 90 ; angle > 0 ; angle-- )
    {
      servo.write(angle);
      delay(30);
    }
  }
  }
*/
void print_data(uint8_t *_data, int _Size);
void get_data_serial(uint8_t *_data, int _Size);
void set_data_serial(uint8_t *_data, int _Size);

void set_signature(uint8_t *p, uint8_t *s);

void setup() {
  // put your setup code here, to run once:
  Serial.begin(57600);
  pinMode(ledPin, OUTPUT);
  pinMode(redLedPin, OUTPUT);
  //  servo.attach(servoPin);

  while (ecc.genEccKey((uint8_t)0, false) != 0);
  memcpy(publicChip, ecc.rsp.getPointer(), 64);

  uECC_set_rng(&RNG);
  uECC_make_key(publicClient, privateClient, curve);

  set_data_serial(publicChip, 64);    // Push Chip key. for Authentication
  delay(3000);
  set_data_serial(publicClient, 64);    // Push Public key. for making session key

  get_data_serial(publicServer, 64);
  if (!uECC_shared_secret(publicServer, privateClient, session, curve)) {
    //   Serial.println("shared_secret() failed");
    return ;
  }

  set_data_serial(session, 32);
}

void loop() {
  /*
    Serial.println();
    print_data(publicChip, 64);
    print_data(publicClient, 64);
    print_data(publicServer, 64);
    print_data(session, 32);
    print_data(plain, 16);
    print_data(signatureClient, 64);
    delay(100000);
  */
  //  set_signature(plain, signatureClient);

  get_data_serial(plain, 16);
  if ( plain[0] == 0 )  // light control
  {
    if ( plain[1] == 0 )  // set value
    {
      onoffLight();
      plain[0] = 0;
    }
    else                  // get setting
    {
      plain[0] = 1;
      plain[1] = (uint8_t)light;
    }
  }
  else if ( plain[0] == 1 )            // window conrtol
  {
    if ( plain[1] == 0 )  // set value
    {
      //  onoffWindow();
      onoffBoiler((int)plain[2]);
      plain[0] = 0;
      plain[2] = 0;
    }
    else                  // get setting
    {
      plain[0] = 1;
      plain[1] = (uint8_t)bright;
    }
  }
  else if ( plain[0] == 2 )            // window conrtol
  {
    if ( plain[1] == 0 )  // set value
    {
      onoffGreenLight();
      plain[0] = 0;
    }
    else                  // get setting
    {
      plain[0] = 1;
      plain[1] = (uint8_t)greenlight;
    }
  }
  set_signature(plain, signatureClient);
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
    Serial.print((int)_data[i]);
    Serial.print(' ');
    if ( (i + 1) % 16 == 0 )
      Serial.print('\n');
  }
  Serial.print('\n');
}

void set_signature(uint8_t *p, uint8_t *s)
{
  ecc.sign(0, p, 16);
  memcpy(s, ecc.rsp.getPointer(), 64);

  set_data_serial(p, 16);
  delay(3000);
  set_data_serial(s, 64);
}


static int RNG(uint8_t *dest, unsigned size)
{
  // Use the least-significant bits from the ADC for an unconnected pin (or connected to a source of
  // random noise). This can take a long time to generate random data if the result of analogRead(0)
  // doesn't change very frequently.
  while (size) {
    uint8_t val = 0;
    for ( i = 0; i < 8; ++i) {
      int init = analogRead(0);
      int count = 0;
      while (analogRead(0) == init) {
        ++count;
      }

      if (count == 0) {
        val = (val << 1) | (init & 0x01);
      } else {
        val = (val << 1) | (count & 0x01);
      }
    }
    *dest = val;
    ++dest;
    --size;
  }
  // NOTE: it would be a good idea to hash the resulting random data using SHA-256 or similar.
  return 1;
}
