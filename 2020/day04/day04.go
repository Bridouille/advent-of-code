package main

import (
	"fmt"
	"strconv"
	"strings"

	"github.com/Bridouille/advent-of-code/utils"
)

type Field struct {
	name       string
	isOptional bool
	isValid    func(string) bool
}

var validPass = []Field{
	Field{"byr", false, func(str string) bool {
		digit, err := strconv.Atoi(str)
		return err == nil && digit >= 1920 && digit <= 2002
	}},
	Field{"iyr", false, func(str string) bool {
		digit, err := strconv.Atoi(str)
		return err == nil && digit >= 2010 && digit <= 2020
	}},
	Field{"eyr", false, func(str string) bool {
		digit, err := strconv.Atoi(str)
		return err == nil && digit >= 2020 && digit <= 2030
	}},
	Field{"hgt", false, func(str string) bool {
		isCm := strings.HasSuffix(str, "cm")
		isInches := strings.HasSuffix(str, "in")
		digit, err := strconv.Atoi(str[:len(str)-2])
		return err == nil && ((isCm && digit >= 150 && digit <= 193) || (isInches && digit >= 59 && digit <= 76))
	}},
	Field{"hcl", false, func(str string) bool {
		_, err := strconv.ParseInt(str[1:], 16, 32)
		return str[0] == '#' && err == nil
	}},
	Field{"ecl", false, func(str string) bool {
		return utils.Contains([]string{"amb", "blu", "brn", "gry", "grn", "hzl", "oth"}, str)
	}},
	Field{"pid", false, func(str string) bool {
		_, err := strconv.Atoi(str)
		return len(str) == 9 && err == nil
	}},
	Field{"cid", true, func(str string) bool { return true }},
}

func analysePassports(lines []string, isValid func(map[string]string) bool) int {
	valids := 0
	passport := make(map[string]string)
	for _, line := range lines {
		if len(line) == 0 { // empty line
			if isValid(passport) {
				valids++
			}
			passport = make(map[string]string)
		} else {
			fields := strings.Split(line, " ")
			for _, str := range fields {
				passport[strings.Split(str, ":")[0]] = strings.Split(str, ":")[1]
			}
		}
	}
	return valids
}

func part1(lines []string) int {
	return analysePassports(lines, func(passport map[string]string) bool {
		for _, field := range validPass {
			_, present := passport[field.name]
			if !present && !field.isOptional {
				return false
			}
		}
		return true
	})
}

func part2(lines []string) int {
	return analysePassports(lines, func(passport map[string]string) bool {
		for _, field := range validPass {
			value, present := passport[field.name]
			if !present && !field.isOptional {
				return false
			}
			if present {
				if !field.isValid(value) {
					return false
				}
			}
		}
		return true
	})
}

func main() {
	fmt.Println("(exemple) part1=", part1(utils.ReadCompleteFile("./inputs/day04_example.txt")))
	fmt.Println("part1=", part1(utils.ReadCompleteFile("./inputs/day04.txt")))
	fmt.Println("(exemple) part2=", part2(utils.ReadCompleteFile("./inputs/day04_example.txt")))
	fmt.Println("part2=", part2(utils.ReadCompleteFile("./inputs/day04.txt")))
}
