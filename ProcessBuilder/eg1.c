#include<stdio.h>
#include<string.h>
int main(int c,char *v[])
{
if(c<=0)
{
printf("Command Line ARGS Required");
return 0;
}
FILE *f=fopen("result.data","w");
for(int i=1;i<c;i++)
{
int l=strlen(v[i]);
fprintf(f,"%s - %d\n",v[i],l);
}
fclose(f);
return 0;
}