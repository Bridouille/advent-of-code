package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

const INPUT_DIR = "inputs"

func generateDay(nb int) {
	day := fmt.Sprintf("day%02d", nb)
	fmt.Printf("Generating %s\n", day)
	if err := os.Mkdir(day, os.ModePerm); err != nil {
		fmt.Printf("Could not create directory %s -> %s", day, err)
		return
	}
	gofile := fmt.Sprintf("%s/%s.go", day, day)
	if file, err := os.Create(gofile); err != nil {
		fmt.Printf("Could not create file %s -> %s", gofile, err)
	} else {
		file.Close()
	}

	inputs := []string{"example.txt", "part1.txt", "part2.txt"}
	for _, input := range inputs {
		filename := fmt.Sprintf("%s/%s_%s", INPUT_DIR, day, input)
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
