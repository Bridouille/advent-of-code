package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

const INPUT_DIR = "inputs"
const TEMPLATE_FILE = "template.txt"
const SRC_DIR = "src"

func generateDay(nb int) {
	day := fmt.Sprintf("day%02d", nb)
	fmt.Printf("Generating %s\n", day)
	if err := os.Mkdir(SRC_DIR + "/" + day, os.ModePerm); err != nil {
		fmt.Printf("Could not create directory %s -> %s", day, err)
		return
	}
	ktFilename := fmt.Sprintf(SRC_DIR + "/%s/%s.kt", day, day)
	kFile, err := os.Create(ktFilename)
	if err != nil {
		fmt.Printf("Could not create file %s -> %s", ktFilename, err)
		return
	}
	defer kFile.Close()
	template, err := os.ReadFile(TEMPLATE_FILE)
	if err != nil {
		panic(err)
	}
	toWrite := strings.ReplaceAll(string(template), "XXX", day)
	if _, err := kFile.Write([]byte(toWrite)); err != nil {
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
