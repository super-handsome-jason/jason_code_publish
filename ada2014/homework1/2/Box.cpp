#include <iostream>
#include <cstdlib>
#include <list>

using namespace std;

struct Box
{
    public:
        long long w,h;
    private:

};
int main(int argc, char* argv[])
{
	list<int> answer_list;
	int number_of_cases = 0;
	scanf ("%d", &number_of_cases);
	for (int i = 0; i < number_of_cases; ++i)
	{
		int number_of_boxes;
		scanf ("%d", &number_of_boxes);
		list<Box> bb;
		//Box bb[number_of_boxes];

		for (int j = 0; j < number_of_boxes; ++j)
		{
			Box b;
			scanf ("%lld%lld", &b.w, &b.h);
			bb.push_back(b);
		}

		int number_of_pairs = 0;
		list<Box>::iterator j, k;
		for (j = bb.begin(); j != bb.end(); ++j)
		{
			for (k = bb.begin(); k != bb.end(); ++k)
			{
				if (j == k)
			  		continue;//not to compare the same box
				if ((j->w <= k->w && j->h <= k->h) ||
				 (j->w <= k->h && j->h <= k->w))
					++number_of_pairs;
			}
		}
		
		answer_list.push_back(number_of_pairs);
	}
	
	//print answer...
	list<int>::iterator i;
	for (i = answer_list.begin(); i != answer_list.end(); ++i)
		cout << *i << "\n"; 
	cout << endl;
}
