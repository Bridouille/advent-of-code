package main

func copyArray(arr [][]int) [][]int {
	cpy := make([][]int, len(arr))
	for i, line := range arr {
		cpy[i] = make([]int, len(line))
		copy(cpy[i], line)
	}
	return cpy
}
