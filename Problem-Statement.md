
# Problem Statement
In this example, we can assume there is a source of data for Wawa orders coming from our stores.  The goal of the exercise it to consume a stream of event data in the JSON format, each JSON document should be considered mutually exclusive from the others.  The data will come in very quickly, in the order of millions of events per day.  The outcome would be a moving average of the item cost by category as well as the average reward of each category.

A few key notes:

·         The order data contains details about the sale as well as a rewards scheme associate with the sale.

·         Each sale category may have a different schema, so drinks may have size and sandwiches may have addons.

·         The rewards are in the form of points, points are calculated via a series (blue, green yellow) of "made up" strategies that takes into account the category and item price.

·         Average is generated based on the item category and would be calculated for both the item price and the item rewards points.

An example event stream is:

{"id":123, "category": "drink" , "price": 10, "rewards_scheme": "blue" , "name": "latte", "size": "large"}
{"id":124, "category": "sandwich", "price": 15, "rewards_scheme": "green" , "name": "turkey sub", addons: ["onions", "lettuce"]}
{"id":125, "category": "retail" , "price": 8.5, "rewards_scheme": "yellow", "name": "snickers bar"}
{"id":128, "category": "drink" , "price": 1.2, "rewards_scheme": "green" , "name": "coke", "size": "medium"}

The outcome may be something like this (For each moving average window):

Category |drink|sandwich|retail

Time|Avg $|Avg Pts
	

Avg $
	

Avg Pts
	

Avg $
	

Avg Pts

Pos 1
	

8.2
	

2
	

15
	

3
	

8.5
	

3

Pos 2
	

8.3
	

3
	

15.1
	

3
	

8.4
	

3

Pos 3
	

8.1
	

2
	

15.5
	

4
	

8.2
	

2

The net result of the processing should be a visualization (UI or Console) of a moving average (https://en.wikipedia.org/wiki/Moving_average) of the average item cost by category and average rewards points calculated by category.

The code should be as close to "production ready" code as possible and should make good use of OO design, appropriate patterns and testing/automation.
