#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>
#include <signal.h>

#include <string.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>

#include <openssl/aes.h>
#include "uECC.h"


//////////////////////////// Variable for Client(android) ////////////////////////////

int serv_sock_c;
int clnt_sock_c;
int str_len_c;
struct sockaddr_in serv_addr_c;
struct sockaddr_in clnt_addr_c;
int clnt_addr_size_c;

char session_c[32] = {113, 207, 82, 165, 5, 12, 247, 225, 17, 235, 145, 181, 244, 98, 10, 238,
			153, 192, 70, 115, 156, 65, 100, 120, 175, 223, 245, 61, 108, 62, 87, 77};

char plain_c[32];
char cypher_c[32];

AES_KEY AES_enc_c;
AES_KEY AES_dec_c;

/////////////////////////// Variable for Arduino /////////////////////////////////////

int serv_sock;
int clnt_sock;
int str_len;
struct sockaddr_in serv_addr;
struct sockaddr_in clnt_addr;
int clnt_addr_size;

char data[80] = {'\0'};
char cypher[16];
char plain[16] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
char sig[64];

const struct uECC_Curve_t *curve;
char private[32];
char public[64];        // my private, public key
char public_client[64]; // client's public key
char auth_public_client[64];    // for authentication
char session[32];       // session key
AES_KEY AES_enc;
AES_KEY AES_dec;        // AES session KEY

/////////////////////////////////////////////////////////////////////////////////////////

int i;


int print_data(char *key, int size)
{
	int i, j;
	for ( i=0 ; i<size ; i++ )
	{
		printf("%4d", key[i]);
		if ( (i+1)%16 == 0 )
			printf("\n");
	}
	printf("\n");
}

void get_data(char *data, int size)
{
	int i;
	for (i=0 ; i<size ; i++ )
		read(clnt_sock, &data[i], 1);	// read client public key
	
}

void ConnectToArduino(char *port);
void ConnectToClient(char *port);
void Start();

int main( int argc, char** argv )
{
	if ( argc != 3 )
	{
		printf("Usage: %s <ArduinoPortNum> <ClientPortNum>\n", argv[0]);
		exit(0);
	}
	curve = uECC_secp256r1();
	
//	ConnectToArduino(argv[1]);

	ConnectToClient(argv[2]);
	
	/////////////////////////////////////////////////// Make Connection ////////////////////////////////////////////////////

//	Start();	// start menu

	close(serv_sock);
	close(serv_sock_c);

	return 1;
}

void Start()
{
	int sel=1;
	while ( sel!=-1 )
	{
		printf("input: ");
		scanf("%d", &sel);

		memset(plain, 0, 16);
		memset(cypher, 0, 16);

		switch(sel)
		{
			case 1:
				plain[0] = 0;
				plain[1] = 0;
				break;
			case 2:
				plain[0] = 0;
				plain[1] = 1;
				break;
			case 3:
				plain[0] = 1;
				plain[1] = 0;
				printf("temperature: ");
				scanf("%d", &sel);
				plain[2] = (char)sel;
				break;
			case 4:
				plain[0] = 1;
				plain[1] = 1;
				break;
			case 5:
				plain[0] = 2;
				plain[1] = 0;
				break;
			case 6:
				plain[0] = 2;
				plain[1] = 1;
				break;
			case -1:
				printf("exit!!!!!\n");
				return;
			default :
				printf("1: light on/off\n2: light setting get\n3: window on/off\n4: window setting get\n\n");
				continue;
		}
		AES_encrypt(plain, cypher, &AES_enc);
		write(clnt_sock, cypher, sizeof(cypher) );		// write order to client

		get_data(data, 80);		// read data from arduino
		memcpy(cypher, data, 16);
		memcpy(sig, data+16, 64);

		AES_decrypt(cypher, plain, &AES_dec);

		if ( uECC_verify(auth_public_client, plain, 16, sig, curve))
		{
		        printf("uECC_verify() failed\n");
		}


		if ( plain[0] == 0 )	// set complete
		{
			printf("set value!!!!\n");
		}
		else			// get setting value
		{
			printf("%d\n", plain[1]);
		}
	}
}

void ConnectToClient(char *port)
{
	serv_sock_c=socket(PF_INET, SOCK_STREAM, 0);
	if(serv_sock_c == -1)
	{
		printf("server(client) socket() error");
		exit(1);
	}
	memset(&serv_addr_c, 0, sizeof(serv_addr_c));
	serv_addr_c.sin_family=AF_INET;
	serv_addr_c.sin_addr.s_addr=htonl(INADDR_ANY);
	serv_addr_c.sin_port=htons(atoi(port));

	if(bind(serv_sock_c, (struct sockaddr*) &serv_addr_c, sizeof(serv_addr_c))==-1)
	{
		printf("server(client) bind() error");
		exit(1);
	}
	if(listen(serv_sock_c, 5)==-1)
	{
		printf("server(client) listen() error");
		exit(1);
	}

	printf("client accept waiting....\n");
	clnt_addr_size_c = sizeof(clnt_addr_c);
	clnt_sock_c = accept(serv_sock_c, (struct sockaddr*)&clnt_addr_c, (socklen_t*)&clnt_addr_size_c);
	if(clnt_sock_c==-1)
	{	
		printf("server(client) accept() error");
		exit(1);
	}
	printf("accepted!!!\n");

	AES_set_encrypt_key(session_c, 256, &AES_enc_c);
	AES_set_decrypt_key(session_c, 256, &AES_dec_c);    // set AES encrypt, decrypt key

//	AES_encrypt(plain_c, cypher_c, &AES_enc_c);
	read(clnt_sock_c, cypher_c, sizeof(cypher_c));			// read data from client
	AES_decrypt(cypher_c, plain_c, &AES_dec_c);

//	write(clnt_sock_c, cypher_c, sizeof(cypher_c) );		// write order to client
	print_data(cypher_c, 32);
	print_data(plain_c, 32);

}

void ConnectToArduino(char *port)
{
	serv_sock=socket(PF_INET, SOCK_STREAM, 0);
	if(serv_sock == -1)
	{
		printf("server(arduino) socket() error");
		exit(1);
	}
	memset(&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family=AF_INET;
	serv_addr.sin_addr.s_addr=htonl(INADDR_ANY);
	serv_addr.sin_port=htons(atoi(port));

	if(bind(serv_sock, (struct sockaddr*) &serv_addr, sizeof(serv_addr))==-1)
	{
		printf("server(arduino) bind() error");
		exit(1);
	}
	if(listen(serv_sock, 5)==-1)
	{
		printf("server(arduino) listen() error");
		exit(1);
	}

	printf("Arduino accept waiting....\n");
	clnt_addr_size = sizeof(clnt_addr);
	clnt_sock = accept(serv_sock, (struct sockaddr*)&clnt_addr, (socklen_t*)&clnt_addr_size);
	if(clnt_sock==-1)
	{
		printf("server(arduino) accept() error");
		exit(1);
	}
	
	if (!uECC_make_key(public, private, curve) )
	{
		printf("uECC_make_key() failed\n");
		exit(1);
	}

	printf("public\n");
	print_data(public, 64);
	printf("private\n");
	print_data(private, 32);


	get_data(auth_public_client, 64);
	printf("client's auth public\n");
	print_data(auth_public_client, 64);

	get_data(public_client, 64);
	printf("client's public\n");
	print_data(public_client, 64);

	str_len=write(clnt_sock, public, sizeof(public) );		// write my public key to client

	if (!uECC_shared_secret(public_client, private, session, curve))	// make session key
	{
		printf("shared_secret() failed\n");
		exit(1);
	}
	AES_set_encrypt_key(session, 256, &AES_enc);
	AES_set_decrypt_key(session, 256, &AES_dec);	// set AES encrypt, decrypt key
	
	printf("session\n");
	print_data(session, 32);
}
