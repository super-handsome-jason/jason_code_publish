#include <iostream>
#include <cstdlib>

using namespace std;

class Box
{
    public:
        long long w,h;
    private:

};
  int main(int argc, char* argv[])
  {
    int number_of_cases = 0;
    scanf ("%d", &number_of_cases);
    for (int i = 0; i < number_of_cases; ++i)
    {
      int number_of_boxes;
      scanf ("%d", &number_of_boxes);
      Box bb[number_of_boxes];
      
      for (int j = 0; j < number_of_boxes; ++j)
      {
        bb[j] = new Box();
        scanf ("%lld%lld", &bb[j].w, &bb[j].h);
      }
      
      int number_of_pairs = 0;
      
      for (int j = 0; j < bb.length; ++j)
      {
      	for (int k = j-1; k < bb.length; ++k)
      	{
      	  if ((bb[j].w >= bb[k].w && bb[j].h >= bb[k].h) ||
      	     (bb[k].w > bb[j].w && bb[k].h > bb[j].h))
      	     ++number_of_pairs;
      	}
      }
      
      printf("%d", number_of_pairs);
    }
  }
