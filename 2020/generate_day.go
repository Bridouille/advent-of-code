package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

const INPUT_DIR = "inputs"
const TEMPLATE_FILE = "template.txt"

func generateDay(nb int) {
	day := fmt.Sprintf("day%02d", nb)
	fmt.Printf("Generating %s\n", day)
	if err := os.Mkdir(day, os.ModePerm); err != nil {
		fmt.Printf("Could not create directory %s -> %s", day, err)
		return
	}
	goFilename := fmt.Sprintf("%s/%s.go", day, day)
	goFile, err := os.Create(goFilename)
	if err != nil {
		fmt.Printf("Could not create file %s -> %s", goFilename, err)
		return
	}
	defer goFile.Close()
	template, err := os.ReadFile(TEMPLATE_FILE)
	if err != nil {
		panic(err)
	}
	toWrite := strings.ReplaceAll(string(template), "XXX", day)
	if _, err := goFile.Write([]byte(toWrite)); err != nil {
		panic(err)
	}

	inputs := []string{"_example.txt", ".txt"}
	for _, input := range inputs {
		filename := fmt.Sprintf("%s/%s%s", INPUT_DIR, day, input)
		if file, err := os.Create(filename); err != nil {
			fmt.Printf("Could not create file %s -> %s", filename, err)
		} else {
			file.Close()
		}
	}
}

func main() {
	args := os.Args[1:]
	if len(args) >= 1 {
		day := args[0]
		if strings.HasPrefix(day, "day") {
			if dayNumber, _ := strconv.Atoi(day[3:]); dayNumber >= 1 && dayNumber <= 25 {
				generateDay(dayNumber)
				return
			}
		}
	}
	fmt.Println("usage: go run generate_day day{1-25}")
}
