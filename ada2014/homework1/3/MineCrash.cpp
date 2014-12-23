#include <iostream>
#include <cstdlib>
#include <list>

using namespace std;

struct Event
{
    public:
    	int l,r;
        long long v;
    private:
};

int main(int argc, char* argv[])
{
// 	list<int> answer_list;

 	int number_of_cases = 0;	
	scanf ("%d", &number_of_cases);
	for (int i = 0; i < number_of_cases; ++i)
	{
		int number_of_players, number_of_blocks, number_of_events;
		scanf ("%d%d%d", &number_of_players, &number_of_blocks, &number_of_events);
		
		list<long long> goals;
		
		for (int j = 0; j < number_of_players; ++j)
		{
			long long gg;
			scanf ("%lld", &gg);
			goals.push_back(gg);
		}
		
		list<long long> ownerships;
		
		for (int j = 0; j < number_of_blocks; ++j)
		{
			long long oo;
			scanf ("%lld", &oo);
			ownerships.push_back(oo);
		}

		list<Event> events;
		
		for (int j = 0; j < number_of_events; ++j)
		{
			Event ee;
			scanf ("%d%d%lld", &ee.l, &ee.r, &ee.v);
			events.push_back(ee);
		}

// 		list<int> answer;
// 		
// 		
// 		list<Event>::iterator j;
// 		for (j = bb.begin(); j != bb.end(); ++j)
// 		{
// 			for (k = bb.begin(); k != bb.end(); ++k)
// 			{
// 				if (j == k)
// 			  		continue;//not to compare the same box
// 				if ((j->w <= k->w && j->h <= k->h) ||
// 				 (j->w <= k->h && j->h <= k->w))
// 					++number_of_pairs;
// 			}
// 		}
// 		
// 		answer_list.push_back(number_of_pairs);

		cout << "goals = " << goals.size() << ", ownerships= " << ownerships.size() << ", events=" << events.size() << endl;  
	}
	
// 	//print answer...
// 	list<int>::iterator i;
// 	for (i = answer_list.begin(); i != answer_list.end(); ++i)
// 		cout << *i << "\n"; 
// 	cout << endl;



}
