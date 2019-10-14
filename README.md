# CSV Wrangler

## Requirements

1. Java 8+
2. maven

## Install and Test 

```
cd csv_wrangler
mvn install
```

## Running the CSV Wrangler

### As a command line

The command line version of the application takes 3 parameters:
1. The input file location
2. The output file location
3. the Config file

Below is an example execution command which uses an example input and config.

```shell script
mvn exec:java -D exec.mainClass=com.gocrisp.assignment.CsvTransformer -Dexec.args="examples/example1/config.yaml examples/example1/input.csv examples/example1/output.csv"
cat examples/example1/output.csv
```

```shell script
mvn exec:java -D exec.mainClass=com.gocrisp.assignment.CsvTransformer -Dexec.args="examples/example2/config.yaml examples/example2/input.csv examples/example2/output.csv"
cat examples/example2/output.csv
```

### As part of your java application

Add the maven dependency

```xml
<dependency>
    <groupId>com.gocrisp</groupId>
    <artifactId>assignment</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Using YAML Config
```java
CsvTransformer csvTransformer = new CsvTransformer();

// Set up input
File config = new File("config.yaml");
File input = new File("input.csv");
File output = new File("output.csv");
Reader reader = Files.newBufferedReader(getPath(input));
Writer writer = Files.newBufferedWriter(getPath(output));

csvTransformer.transformCsv(config, reader, writer);
```

### Using Object Model Config
```java
CsvTransformer transformer = new CsvTransformer();
// Set up input
File config = new File("config.yaml");
File input = new File("input.csv");
File output = new File("output.csv");
Reader reader = Files.newBufferedReader(getPath(input));
Writer writer = Files.newBufferedWriter(getPath(output));

// Build Transformation Configuration
Command readCommand = new ReadColumn("Order Number");
ColumnTransformer columnTransformer = new ColumnTransformer("OrderID", Collection.singletonList(readCommand));
RowTransformer rowTransformer = new RowTransformer(Collection.singletonList(columnTransformer));

transformer.transformCsv(rowTransformer, reader, writer);
```

For more clarity on defining the object model config please check the `javadoc` folder.

## Understanding the Config YAML Format

The config takes a series of output columns. For each column it provides a list of commands on how to generate the result. Each command has a different set of properties.

Here is a simple example:
```yaml
columns:
  - name: "OrderID"
    commands:
      - name: "read"
        column_name: "Order Number"
```
This yaml will produce an output csv with one column, where for each row in the input csv it reads the "Order Number" column and outputs it to the OrderID column in the output csv.

### Available Commands

#### Read the raw value into the output column

```yaml
name: "value"
value: "kg"
```

#### Read the  value from a specified input column

```yaml
name: "read"
column_name: "Order Number"
```

#### Parse the output as a date format

```yaml
name: "parse_date"
input_format: "yyyy-mm-dd"
output_format: "dd-mm-yyyy"
```

#### Parse the output as a formatted number

```yaml
name: "parse_number"
input_country: "en"
input_lang: "GB"
output_country: "de"
output_lang: "DE"
is_grouping_used: true # optional
min_decimal_places: 2 # optional
max_decimal_places: 2 # optional
```

#### Performs a regex on current output and returns the given group. 

```yaml
name: "perform_regex"
regex: "PC-(\\d*)"
group: 1 #optional (default 0)
```


## Architectural Choices

1. I decided to write this in Java, simply as that is a language I am most familiar with. I decided to use Java 8 as opposed to later versions as I would like it to be usable as a plugable library and many projects haven't upgraded to later versions of java yet. Whereas future java versions should be backwards compatible with Java 8.
2. I decided to expose two public methods in my main transformer class, one which transforms from YAML file and one which takes the models directly. The reason I chose YAML as a config file, is that it is very human readable. The reason I chose to have an option to use models directly is that applications using this library would be able to implement custom commands that are not included but are very specific to their use case.
3. I decided that rather than adding commands to add/rename/delete columns from the input row to produce an output, instead to just have a list of columns to add to the output, and commands which can read the current row from the input. This seemed to be flexible enough and provide a simpler DSL for transformations.
4. Initially I made the `transformCsv` method take a `File` object for input and output, but then changed this to just provide `Reader` and `Writer` respectively, as these CSVs may be read/written from a socket for example.

## Assumptions

1. Regarding the "no significant dependencies", I used two main dependencies in my project: opencsv and jackson for reading YAML file into objects. I hope that this isn't considered significant. Besides this other dependencies are just for test scope.
2. The number of potential transformations is limitless, I only implemented transformations that handled the example given in the briefing plus one or two more for demonstration. My assumption was that giving users the freedom to use the Object based DSL to pass in their own implementations of the `Command` interface would give good flexibility. I did not assume the same for config file.
3. While I have implemented a main function to allow this to be run from the command line for demo purposes, I assumed that it would primarily be used as a library by other projects.

## Next Steps

Of course if this were a real project how it would grow would heavily depend on how it is used. Here are a couple of ideas though:

1. Handlers for different input formats such as json, yaml etc.
2. Ability for clients to write their own output handler which can perform actions on the transformed row. E.g instead of outputting the result to a CSV file, they may want to send results in batch to a RESTful API for reporting.
3. Perhaps the use case to provide multiple inputs that can be merged together.

