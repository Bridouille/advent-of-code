package main

import (
	"fmt"
	"strings"

	"github.com/Bridouille/advent-of-code/utils"
)

// "muted_yellow" => {shiny_gold: 2, faded_blue: 4}
func buildBagMap(lines []string) map[string]map[string]int {
	ret := make(map[string]map[string]int)
	for _, line := range lines {
		container := strings.Split(line, "contain")[0]
		containee := strings.Split(strings.Split(line, "contain")[1], ",")
		containerName := strings.Split(container, " ")
		containerKey := containerName[0] + "_" + containerName[1]
		ret[containerKey] = make(map[string]int)
		if containee[0] == " no other bags." {
			continue
		}

		for _, containee := range containee {
			containeeName := strings.Split(strings.Trim(containee, " ."), " ")
			containeeKey := containeeName[1] + "_" + containeeName[2]
			ret[containerKey][containeeKey] = utils.ToInt(containeeName[0])
		}
	}
	return ret
}

func bagContaining(m map[string]map[string]int, key string) int {
	count := 0
	delete(m, key)
	for bagName, bagsInside := range m {
		if _, present := bagsInside[key]; present {
			count += 1 + bagContaining(m, bagName)
		}
	}
	return count
}

func sumOfContainedBags(m map[string]map[string]int, key string) int {
	count := 1
	contained, ok := m[key]
	if !ok {
		return 0
	}
	for name, amount := range contained {
		count += sumOfContainedBags(m, name) * amount
	}
	return count
}

func part1(lines []string) int {
	return bagContaining(buildBagMap(lines), "shiny_gold")
}

func part2(lines []string) int {
	return sumOfContainedBags(buildBagMap(lines), "shiny_gold") - 1
}

func main() {
	fmt.Println("(exemple) part1=", part1(utils.ReadFile("./inputs/day07_example.txt")))
	fmt.Println("part1=", part1(utils.ReadFile("./inputs/day07.txt")))
	fmt.Println("(exemple) part2=", part2(utils.ReadFile("./inputs/day07_example.txt")))
	fmt.Println("part2=", part2(utils.ReadFile("./inputs/day07.txt")))
}
