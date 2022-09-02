package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

func part1(lines []string) int {
	total := 0
	for _, line := range lines {
		parts := strings.Split(line, "|")

		if len(parts) >= 1 {
			digits := strings.Split(strings.TrimSpace(parts[1]), " ")
			for _, digit := range digits {
				nbDigits := len(digit)
				if 2 == nbDigits || 4 == nbDigits || 3 == nbDigits || 7 == nbDigits {
					total++
				}
			}
		}
	}
	return total
}

type Digit struct {
	RealDigit int
	Length    int
	Matcher   func(string, map[int]string) bool
}

func findDigit(digit Digit, alreadyFound map[int]string, encodedNumbers []string) int {
	for idx, nb := range encodedNumbers {
		if len(nb) == digit.Length && digit.Matcher(nb, alreadyFound) {
			return idx
		}
	}
	return -1
}

func intersect(a string, b string) int {
	total := 0
	for _, char := range b {
		total += strings.Count(a, string(char))
	}
	return total
}

// 1 => len() == 2
// 4 => len() == 4
// 7 => len() == 3
// 8 => len() == 7

// 3 => len() == 5 && contains(2) from 1 && contains(3) from 7
// 6 => len() == 6 && contains(1) from 1 && contains(2) from 7

// 2 => len() == 5 && contains(1) from 1 && contains(2) from 7 && contains(4) from 6
// 5 => len() == 5 && contains(1) from 1 && contains(2) from 7 && contains(5) from 6

// 9 => len() == 6 && contains(2) from 1 && contains(3) from 7 && contains(5) from 3
// 0 => len() == 6 && contains(2) from 1 && contains(3) from 7 && contains(4) from 3
func fillNumber(toFind int, alreadyFound map[int]string, encodedNumbers []string) int {
	finds := []Digit{
		Digit{RealDigit: 0, Length: 6, Matcher: func(candidate string, alreadyFound map[int]string) bool {
			return intersect(candidate, alreadyFound[1]) == 2 && intersect(candidate, alreadyFound[7]) == 3 && intersect(candidate, alreadyFound[3]) == 4
		}},
		Digit{RealDigit: 1, Length: 2, Matcher: func(string, map[int]string) bool { return true }},
		Digit{RealDigit: 2, Length: 5, Matcher: func(candidate string, alreadyFound map[int]string) bool {
			return intersect(candidate, alreadyFound[1]) == 1 && intersect(candidate, alreadyFound[7]) == 2 && intersect(candidate, alreadyFound[6]) == 4
		}},
		Digit{RealDigit: 3, Length: 5, Matcher: func(candidate string, alreadyFound map[int]string) bool {
			return intersect(candidate, alreadyFound[1]) == 2 && intersect(candidate, alreadyFound[7]) == 3
		}},
		Digit{RealDigit: 4, Length: 4, Matcher: func(string, map[int]string) bool { return true }},
		Digit{RealDigit: 5, Length: 5, Matcher: func(candidate string, alreadyFound map[int]string) bool {
			return intersect(candidate, alreadyFound[1]) == 1 && intersect(candidate, alreadyFound[7]) == 2 && intersect(candidate, alreadyFound[6]) == 5
		}},
		Digit{RealDigit: 6, Length: 6, Matcher: func(candidate string, alreadyFound map[int]string) bool {
			return intersect(candidate, alreadyFound[1]) == 1 && intersect(candidate, alreadyFound[7]) == 2
		}},
		Digit{RealDigit: 7, Length: 3, Matcher: func(string, map[int]string) bool { return true }},
		Digit{RealDigit: 8, Length: 7, Matcher: func(string, map[int]string) bool { return true }},
		Digit{RealDigit: 9, Length: 6, Matcher: func(candidate string, alreadyFound map[int]string) bool {
			return intersect(candidate, alreadyFound[1]) == 2 && intersect(candidate, alreadyFound[7]) == 3 && intersect(candidate, alreadyFound[3]) == 5
		}},
	}
	return findDigit(finds[toFind], alreadyFound, encodedNumbers)
}

func part2(lines []string) int {

	total := 0
	for _, line := range lines {
		numbers := make(map[int]string)
		parts := strings.Split(line, "|")

		encodedNumbers := strings.Split(strings.TrimSpace(parts[0]), " ")
		toFind := []int{1, 4, 7, 8, 3, 6, 2, 5, 9, 0} // The order in which we find the unique numbers
		for _, nbToFind := range toFind {
			idx := fillNumber(nbToFind, numbers, encodedNumbers)
			numbers[nbToFind] = encodedNumbers[idx] // store for later & remove from the list
			encodedNumbers = append(encodedNumbers[:idx], encodedNumbers[idx+1:]...)
		}

		digits := strings.Split(strings.TrimSpace(parts[1]), " ")
		printedNumber := 0
		for _, digit := range digits {
			for k, v := range numbers {
				if sortString(digit) == sortString(v) {
					printedNumber = printedNumber*10 + k
				}
			}
		}
		total += printedNumber
	}
	return total
}

func main() {

	if len(os.Args) < 2 {
		fmt.Println("Usage: go run {file.go} {filename}")
		return
	}

	file, err := os.Open(os.Args[1])
	if err != nil {
		fmt.Println("Unable to open file", err)
		return
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	lines := make([]string, 0)
	for scanner.Scan() {
		l := strings.TrimSpace(scanner.Text())
		if len(l) > 0 {
			lines = append(lines, l)
		}
	}
	fmt.Printf("part1 => %d\n", part1(lines))
	fmt.Printf("part2 => %d\n", part2(lines))
}
